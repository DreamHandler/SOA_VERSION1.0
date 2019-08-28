package SOA.Port.DataService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import SOA.Base.dataBase.JdbcBuilder;
import SOA.Util.Model.StaticObj;

public class IserviceImpl extends JdbcBuilder{
	@SuppressWarnings("finally")
	public String Excute(String content) {
		String str = "";
		/*BASE64Encoder encode = new BASE64Encoder();
		BASE64Decoder decode = new BASE64Decoder();*/
		try {
			String param = content;
			Document doc = DocumentHelper.parseText(param);
			//验证是否有权限权限操作
			Element root = doc.getRootElement();
			if(!chackQX(root)){
				throw new Exception("无权限访问改后台");
			}
			Element msh = root.element("MSH");
		    String state = msh.element("MSH.2").getText();
		    Element data = msh.element("DATAS");
		    String SQL = data.attributeValue("SQL");
		    if(state==null||SQL==null||"".equals(state)||"".equals(SQL)){
		    	throw new Exception("未传入完整数据");
		    }
		    @SuppressWarnings("unchecked")
			List<Element> datas = data.elements("DATA");
		    ArrayList<String> Cvalue  = null;
		    if(datas != null&&datas.size()>0){
		    	Cvalue = new ArrayList<String>();
		    	for(Element val :datas){
		    		String Values = val.attributeValue("Value");
		    		Cvalue.add(Values);
		    	}
		    }
		    switch (state) {
			case "modify":
				str = String.valueOf(this.ExecModel(SQL, Cvalue));
				str = StaticObj.RtnStrValue(str);
				break;
			case "quer":
				str = this.ExecQryXml(SQL, Cvalue).asXML();
				break;
			default:
				break;
			}
		} catch (DocumentException e) {
			str = StaticObj.RtnExcFail(e);
		}finally{
			try {
				this.ReturnConn();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return str;
		}
	}
	private boolean chackQX(Element root) throws Exception{
		Element CZY = root.element("CZY");
		String UserName = CZY.attributeValue("UserName");
		if(UserName == null||"".equals(UserName)){
			return false;
		}else{
			List<String> list = new ArrayList<String>();
			list.add(UserName);
			ResultSet rs = this.ExecQry("SELECT * FROM BASEMENT..TBUSER WHERE VUSER = ?",list);
			if(rs.next()){
				rs.close();
				return true;
			}else{
				rs.close();
				return false;
			}
			
		}
	}
	@SuppressWarnings("finally")
	public String Login(String content){
		String str = "";
		/*BASE64Encoder encode = new BASE64Encoder();
		BASE64Decoder decode = new BASE64Decoder();*/
		try {
			String param = content;
			Document doc = DocumentHelper.parseText(param);
			Element root = doc.getRootElement();
			Element data = root.element("DATA");
			String UserName = data.attributeValue("UserName");
			String SysNo = data.attributeValue("SysNo");
			if(UserName == null||SysNo == null||"".equals(UserName)||"".equals(SysNo)){
				throw new Exception("未传入完整数据");
			}
//			String QSQL = "SELECT VJOBNUM,VUSER,VPSWD,CZY.VNAME,VLEVEL,"
//					+ "CZY.VascNum,CZY.VascName,VascCpyNum,VascCpyName,CZY.VBZ,VLEBEL,"
//					+ "JB.VNAME JBNAME,BENABLE,JB.VBZ JBBZ,CZYZ.VascNum CZYZVASCNUM,"
//					+ "CZYZ.VascName VZYZVASCNAME,VQXBM,VCDBM,CZYZ.VBZ CZYZVBZ "
//					+ "FROM BASEMENT..TBUSER CZY WITH(NOLOCK) "
//					+ "LEFT JOIN BASEMENT..TBGROUP CZYZ WITH(nolock) ON CZY.VascNum=CZYZ.VascNum "
//					+ "LEFt JOIN  BASEMENT..TBLEVEL JB WITH(NOLOCK) ON CZY.VLEVEL=JB.VLEBEL "
//					+ "WHERE JB.BENABLE =1 AND CZY.VUSER = ? AND CZYZ.VQXBM like ?";
			//先取操作员权限，如果权限不存在，再取操作员组权限
			String QSQL = " SELECT TOP 1 * FROM (SELECT QX.VSYSNO,QX.VCDBM,CZY.VPSWD,CZY.VUSER,CZY.VNAME,CZY.VJOBNUM,CZY.VascNum,CZY.VascName "
					+ " FROM BASEMENT..TBUSERQX QX WITH(NOLOCK) "
					+ " LEFT JOIN BASEMENT..TBUSER CZY WITH(NOLOCK) ON QX.VJOBNUM=CZY.VJOBNUM "
					+ " WHERE CZY.VUSER=? and QX.VSYSNO=?"
					+ " UNION ALL SELECT QX.VSYSNO,QX.VCDBM,CZY.VPSWD,CZY.VUSER,CZY.VNAME,CZY.VJOBNUM,CZY.VascNum,CZY.VascName "
					+ " FROM BASEMENT..TBGROUPQX QX WITH(NOLOCK) "
					+ " LEFT JOIN BASEMENT..TBGROUP CZYZ WITH(NOLOCK) ON QX.VascNum=CZYZ.VascNum "
					+ " LEFT JOIN BASEMENT..TBUSER CZY WITH(NOLOCK) ON QX.VascNum=CZY.VascNum "
					+ " WHERE QX.VSYSNO=? AND CZY.VUSER=? )S";
			
			ArrayList<String> Cvalue = new ArrayList<String>();
			Cvalue.add(UserName);
			Cvalue.add(SysNo);
			Cvalue.add(SysNo);
			Cvalue.add(UserName);
			str = this.ExecQryXml(QSQL, Cvalue).asXML();
		} catch (Exception e) {
			e.printStackTrace();
			str = StaticObj.RtnExcFail(e);
		}finally{
			try {
				this.ReturnConn();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return str;
		}
	}

}
