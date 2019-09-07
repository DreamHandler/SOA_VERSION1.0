package SOA.Util.Model;

import java.sql.ResultSet;

/**
 * 系统参数对象
 * @author qzy
 *
 */
public class Xtcs extends modeUtil{
	protected String VNO;//编码
	protected String VNAME;//参数中文描述
	protected String VSYSNO;//系统编码，用“|”分隔
	protected String VPARAMNAME;//参数调用名称
	protected String VVALUE;//参数值
	@Override
	public void ReadByDb(ResultSet inResultSet) throws Exception {
		setVNO(GetRSString(inResultSet, "VNO"));
		setVNAME(GetRSString(inResultSet, "VNAME"));
		setVSYSNO(GetRSString(inResultSet, "VSYSNO"));
		setVPARAMNAME(GetRSString(inResultSet, "VPARAMNAME"));
		setVVALUE(GetRSString(inResultSet, "VVALUE"));
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
	public String getVSYSNO() {
		return VSYSNO;
	}
	public void setVSYSNO(String vSYSNO) {
		VSYSNO = vSYSNO;
	}
	public String getVPARAMNAME() {
		return VPARAMNAME;
	}
	public void setVPARAMNAME(String vPARAMNAME) {
		VPARAMNAME = vPARAMNAME;
	}
	public String getVVALUE() {
		return VVALUE;
	}
	public void setVVALUE(String vVALUE) {
		VVALUE = vVALUE;
	}
}
