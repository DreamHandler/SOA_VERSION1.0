package SOA.Util.Model;

import java.sql.ResultSet;
//import java.util.List;

import SOA.Base.dataBase.JdbcBuilder;


public class XtParam {
	public static synchronized String GetSysParameter(String VBH,int Step) throws Exception{
		String param = null;
		int ILength = 0;
		JdbcBuilder ADbOpr = new JdbcBuilder();
		ResultSet rs;
		try{
			try {
				String CSQL = "UPDATE BASEMENT..TBLYPARAM SET VVALUE = VVALUE + 1 WHERE VBH='"+VBH+"'";
//				List<String> wherelis = null;
//				wherelis.add(VBH);
				if(ADbOpr.ExecModel(CSQL, null) != 1){
//				if(ADbOpr.Execute(CSQL, null)){
					throw new Exception("更新流水号失败，流水号编码为：！" + VBH);
				}
				rs = ADbOpr.ExecQry("SELECT VVALUE PARAM,ILength FROM BASEMENT..TBLYPARAM WHERE VBH='"+VBH+"'", null);
				if(rs.next()){
					ILength = rs.getInt("ILength");
					param = rs.getString("PARAM");
				}
				if(rs != null)
					rs.close();
			} catch (Exception e) {
				throw e;
			} finally {
				ADbOpr.ReturnConn();
			}
		}catch (Exception e) {
			throw new Exception(VBH + "获取失败！");
		}
	 	return getParamFill(param,ILength);
	}
	/**
	 * 流水号补位; 
	 * paramValue 流水号值 
	 */
	public static synchronized String getParamFill(String paramValue,int fillCount) {
		//位数差
		int count = fillCount - paramValue.length();
		String resultStr = "";
		if (count > 0) {
			for (int i = 0; i < count; i++) {
				resultStr += "0";
			}
			return resultStr += paramValue;
		}
		return paramValue;
	}
}
