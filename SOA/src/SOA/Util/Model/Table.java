package SOA.Util.Model;

import java.sql.ResultSet;


/**
 * 数据表信息配置表
 * @author qzy
 *
 */
public class Table extends modeUtil{
	protected String VNO;//编码
	protected String VNAME;//表信息中文描述
	protected String VTABLENAME;//表名称
	protected String VDATABASE;//库名称
	protected int ITYPE;//表类型
	protected String VKEY;//表主键信息
	
	/**
	 * 初始化数据
	 */
	public void ReadByDb(ResultSet inResultSet) throws Exception {
		setVNO(GetRSString(inResultSet, "VNO"));
		setVNAME(GetRSString(inResultSet, "VNAME"));
		setVTABLENAME(GetRSString(inResultSet, "VTABLENAME"));
		setVDATABASE(GetRSString(inResultSet, "VDATABASE"));
		setITYPE(inResultSet.getInt("ITYPE"));
		setVKEY(GetRSString(inResultSet, "VKEY"));
	}
	/**
	 * 获得具体表名
	 * @param FKeyValue 时间
	 * @return
	 * @throws Exception
	 */
	public String GetTbName(String FKeyValue ) throws Exception{
		 String Result = "";
		 //String Tmp = "";
		 if ((("".equals(FKeyValue)) || (null == FKeyValue)) && (ITYPE != 0)) {
			 return "";
		 }
		 switch (ITYPE) {
		 	case 0 :
			 	Result = VDATABASE + ".." + VTABLENAME;
			 	break;
		 	case 1 :  //单库年表
    	    	Result = VDATABASE + ".." + VTABLENAME + FKeyValue.substring(0, 4);
    	    	break;
		 	case 2 : //年库年表
		 		Result = VDATABASE +FKeyValue.substring(0, 4)+ ".." + VTABLENAME + FKeyValue.substring(0, 4);
		 		break;
		 	case 3 : //年库年月表
		 		Result = VDATABASE +FKeyValue.substring(0, 4)+ ".." + VTABLENAME  + FKeyValue.substring(0, 4) + FKeyValue.substring(5, 7);
		 		break;
		 	 default :
	    		  Result =  "";
		 }
		 return Result;
	}
	public String getVNO() {
		return VNO;
	}
	public void setVNO(String vNO) {
		VNO = vNO;
	}
	public String getVNAME() {
		return VNAME;
	}
	public void setVNAME(String vNAME) {
		VNAME = vNAME;
	}
	public String getVTABLENAME() {
		return VTABLENAME;
	}
	public void setVTABLENAME(String vTABLENAME) {
		VTABLENAME = vTABLENAME;
	}
	public String getVDATABASE() {
		return VDATABASE;
	}
	public void setVDATABASE(String vDATABASE) {
		VDATABASE = vDATABASE;
	}
	public int getITYPE() {
		return ITYPE;
	}
	public void setITYPE(int iTYPE) {
		ITYPE = iTYPE;
	}
	public String getVKEY() {
		return VKEY;
	}
	public void setVKEY(String vKEY) {
		VKEY = vKEY;
	}
}
