package SOA.Base.dataBase;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import SOA.Util.Model.StaticObj;

/**
 * 数据库操作基类
 * @author Administrator
 *
 */
public class JdbcBuilder extends StaticObj {
	private Connection conn = null;
	public Connection getConn() throws SQLException {
		this.setConn();
		if(this.conn == null||conn.isClosed()){
			throw new SQLException("未取到数据库连接！");
		}
		return this.conn;
	}
	
	public void setConn() throws SQLException {
		conn = DBCP.getConnection();
	}
	
	/**
	 * 返回List集合，且集合中存储的Map对象
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String,Object>> getListForMap(String sql, Object... params) throws SQLException {
		Connection conn = this.getConn();
		QueryRunner queryRunner = new QueryRunner();
		List<Map<String,Object>> list = queryRunner.query(conn, sql, new MapListHandler(), params);
		DBCP.closeConnection(conn);
		return list;
	}
	
	/**
	 * 封装删除，编辑，新增等操作
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public int updateAll(String sql, Object... params) throws SQLException {
		Connection conn = this.getConn();;
		QueryRunner queryRunner = new QueryRunner();
		int i = queryRunner.update(conn, sql, params);
		DBCP.closeConnection(conn);
		return i;
	}
	/**
	 * 返回Map集合
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public Map<String,Object> getForMap(String sql, Object... params) throws SQLException {
		Connection conn = this.getConn();
		QueryRunner queryRunner = new QueryRunner();
		Map<String,Object> map = queryRunner.query(conn, sql, new MapHandler(), params);
		DBCP.closeConnection(conn);
		return map;
	}
	
	/**
	 * 从数据库取出一个对象，在进行封装
	 * 注：数据库的列名对应对象的属性名
	 * @param sql
	 * @param obj
	 * @return
	 * @throws SQLException
	 */
	public <T> T getModel(String sql,Class<T> obj,Object... params) throws SQLException{
		Connection conn = this.getConn();
		QueryRunner queryRunner = new QueryRunner();
		
		T t = queryRunner.query(conn, sql,new BeanHandler<T>(obj),params); 
		DBCP.closeConnection(conn);
		return t;
	}
	/**
	 * 从数据库取出一个对象，在进行封装,最后封装到一个List中
	 * 注：数据库的列名对应对象的属性名
	 * @param sql
	 * @param obj
	 * @return
	 * @throws SQLException
	 */
	public <T>List<T> getModelList(String sql,Class<T> obj,Object... params) throws SQLException{
		Connection conn = this.getConn();
		QueryRunner queryRunner = new QueryRunner();
		List<T> list = queryRunner.query(conn, sql, new BeanListHandler<T>(obj), params);
		DBCP.closeConnection(conn);
		return list;
	}
	/**
	 * 查询封装DOM
	 * @param sql
	 * @param Cvaluse
	 * @return
	 * @throws Exception 
	 */
	public Document ExecQryXml(String sql,List<?> Cvaluse)throws Exception{
		ResultSet rs = null;
		ResultSetMetaData rstr = null;
		Document document = null;
		Connection conn = this.getConn();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			if(Cvaluse != null){
				for(int i = 1;i <= Cvaluse.size();i++){
					ps.setObject(i, Cvaluse.get(i-1));
				}
			}
			rs = ps.executeQuery();
			document = DocumentHelper.createDocument();
			Element rsElement = document.addElement("xlm");
			rstr = rs.getMetaData();
			Element fields = rsElement.addElement(Dom_Explains);
			for (int i = 1; i <= rstr.getColumnCount(); i++) {
				Element field = fields.addElement(Dom_Explain);
				//field.addAttribute("ColName", rstr.getCatalogName(i));
				field.addAttribute("ColName", rstr.getColumnLabel(i));
				field.addAttribute("TypeName", rstr.getColumnTypeName(i));
				field.addAttribute("MaxLength", String.valueOf(rstr.getColumnDisplaySize(i)));
			}
			Element value = rsElement.addElement(Dom_Values);
			while(rs.next()){
				Element fieldvalue = value.addElement(Dom_Value);
				for(int i = 1;i<=rstr.getColumnCount(); i++){
					fieldvalue.addAttribute(rstr.getColumnLabel(i),(rs.getString(i)==null?"":rs.getString(i)));
				}
			}
			
			if(rs!=null){
				rs.close();
				rs = null;
			}
		}catch (Exception e) {
			throw new Exception(e.getMessage() + Enter + "   SQL:  " + LogSQL(sql, (ArrayList<?>) Cvaluse));
		}finally{
			if(ps != null){
				ps.close();
				ps=null;
			}
			this.ReturnConn();
		}
		return document;
	}
	
	/**
	 * 普通查询方法
	 * @param sql
	 * @param Cvaluse
	 * @return
	 * @throws Exception 
	 */
	public ResultSet ExecQry(String sql,List<?> Cvaluse)throws Exception{
		ResultSet rs = null;
		Connection conn = this.getConn();
		try{
			PreparedStatement ps = conn.prepareStatement(sql);
			if(Cvaluse != null){
				for(int i = 1;i <= Cvaluse.size();i++){
					ps.setObject(i, Cvaluse.get(i-1));
				}
			}
			rs = ps.executeQuery();
		}catch (Exception e) {
			throw new Exception(e.getMessage() + Enter + "   SQL:  " + LogSQL(sql, (ArrayList<?>) Cvaluse));
		}
		return rs;
	}
	/**
	 * 打印SQL
	 * @param sql
	 * @param List
	 * @return
	 */
	public String LogSQL(String sql, ArrayList<?> List) {
		if (List == null) return sql;
		for (int i = 0; i < List.size(); i++) {
			try{
				sql = sql.replaceFirst("\\?", " " + (List.get(i) == null ? "''" : "'"+List.get(i)+"'"));
			}catch (Exception e) {
				sql = sql.replaceFirst("\\?","'数据中含有非法字符，未替换成功'");
			}
		}
		return sql;
	}
	
	/**
	 * 普通修改方法（返回修改的条数）
	 * @param sql
	 * @param Cvaluse
	 * @return
	 * @throws Exception 
	 */
	public int ExecModel(String sql,List<?> Cvaluse)throws Exception{
		int number = 0;
		Connection conn = this.getConn();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			if(Cvaluse != null){
				for(int i = 1;i <= Cvaluse.size();i++){
					ps.setObject(i, Cvaluse.get(i-1));
				}
			}
			number = ps.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e.getMessage() + Enter + "   SQL:  " + LogSQL(sql, (ArrayList<?>) Cvaluse));
		}
		return number;
	}
	/**
	 * 自带事务修改
	 * @param sql
	 * @param Cvaluse
	 * @param operator
	 * @return
	 * @throws Exception
	 */
	public boolean Execute(String sql, List<?> Cvaluse) throws Exception {
		Connection conn = this.getConn();
		boolean bool = false;
		try {
			conn.setAutoCommit(false);
			PreparedStatement ps = conn.prepareStatement(sql);
			if (Cvaluse != null) {
				for (int i = 1; i <= Cvaluse.size(); i++) {
					ps.setObject(i, Cvaluse.get(i - 1));
				}
			}
			ps.executeUpdate();
			conn.commit();
			bool = true;
		} catch (Exception e) {
			conn.rollback();
			bool = false;
			throw new Exception(e.getMessage() + Enter + "   SQL:  " + LogSQL(sql, (ArrayList<?>) Cvaluse));
		}finally {
			conn.setAutoCommit(true);
			this.ReturnConn();
		}
		return bool;
	}
	/**
	 * 带事务批处理（只要没修改到都算失败）
	 * @param conn
	 * @param CSQL
	 * @param Cvaluse
	 * @return
	 * @throws Exception
	 */
	public boolean ExecuteBatch(Connection conn, String CSQL, List<?> Cvaluse) throws Exception {
		boolean bool = false;
		try{
			PreparedStatement ps = conn.prepareStatement(CSQL);
			if (Cvaluse != null) {
				for (int i = 1; i <= Cvaluse.size(); i++) {
					ps.setObject(i, Cvaluse.get(i - 1));
				}
			}
			ps.addBatch();
			int [] result = ps.executeBatch();
			if( result.length < 0 ){ //执行失败
				throw new Exception("语句执行失败");
			}else
				bool = true;
		}catch (Exception e) {
			bool = false;
			throw new Exception(e.getMessage() + Enter + "   SQL:  " + LogSQL(CSQL, (ArrayList<?>) Cvaluse));
		}
		return bool;
	}
	/**
	 * 外部事务
	 * @param conn
	 * @param sql
	 * @param Cvaluse
	 * @param inOpr
	 * @return
	 * @throws Exception
	 */
	public boolean Execute(Connection conn, String sql, List<?> Cvaluse) throws Exception {
		boolean bool = false;
		try{
			PreparedStatement ps = conn.prepareStatement(sql);
			if (Cvaluse != null) {
				for (int i = 1; i <= Cvaluse.size(); i++) {
					ps.setObject(i, Cvaluse.get(i - 1));
				}
			}
			ps.executeUpdate();
			bool = true;
		}catch (Exception e) {
			bool = false;
			throw new Exception(e.getMessage() + Enter + "   SQL:  " + LogSQL(sql, (ArrayList<?>) Cvaluse));
		}
		return bool;
	}
	/**
	 * 执行存储过程
	 * @param args
	 * @throws Exception 
	 */
	public Object ExecProc(String sql) throws Exception{
		Connection execconn = this.getConn();
		Object obj = null;
		try{
			CallableStatement ps = execconn.prepareCall(sql);
			ps.execute();
			while(ps.getMoreResults() || ps.getUpdateCount() != -1){
				if(ps.getUpdateCount() != -1){
					//System.out.println(aa.getUpdateCount());
				}else{
					obj = ps.getResultSet();
				}
			}
		}catch (Exception e) {
			throw new Exception(e.getMessage() + Enter + "   SQL:  " + sql );
		}
		return obj;
	}
	/**
	 * 归还该对象的数据库链接
	 * @throws SQLException
	 */
	public void ReturnConn() throws SQLException{
		DBCP.closeConnection(this.conn);
	}
	/**
	 * 获取一个新的链接
	 * @return
	 * @throws SQLException
	 */
	public Connection GetNewConn() throws SQLException{
		Connection conn = DBCP.getConnection();
		return conn;
	}
	
}	
