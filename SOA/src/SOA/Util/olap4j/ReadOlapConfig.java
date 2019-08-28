package SOA.Util.olap4j;

import SOA.Base.dataBase.JdbcBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: QZY
 * \* Date: 2019/8/27
 * \* Time: 16:14
 * \
 */
public class ReadOlapConfig {

    protected static String CheckString(ResultSet rs,String FileName) throws Exception{
        String value = rs.getString(FileName);
        return ((value == null) || "null".equals(value))? "": value;
    }

    /**
     * 获得数据库的配置信息
     * **/
    public static Properties getConfig(String ConnNO) throws SQLException {
        //OLAP4J连接
        JdbcBuilder ADbOpr = new JdbcBuilder();
        Properties info = null;
        try{
            String CSQL = "SELECT * FROM BASEMENT..TBZDBIDS With(NoLock) WHERE CBM='"+ConnNO+"'";
            ResultSet rs = ADbOpr.ExecQry(CSQL, null);
            if(rs.next()){
                if(rs.getInt("ILX") == 2){ //必须是多维数据集连接
                    info = new Properties();
                    info.setProperty("Class", "org.olap4j.driver.xmla.XmlaOlap4jDriver");
                    info.setProperty("Driver", "jdbc:xmla:Server="+CheckString(rs,"CXMLAURL")+";Catalog="+CheckString(rs,"CCSSJKMC"));
                    //可增加cube;Cube="+hm.get("Cube");
                    info.put("user", CheckString(rs,"CCZY"));
                    info.put("password", CheckString(rs,"CMM"));
                }
            }
            rs.close();
        }catch (Exception e) {
            new Exception("查询BASEMENT..TBZDBIDS表失败，请检查是否配置正确，错误信息。\n"+e.getMessage());
        }finally{
            ADbOpr.ReturnConn();
            ADbOpr = null;
        }
        return info;
    }

}
