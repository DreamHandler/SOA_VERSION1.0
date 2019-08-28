package SOA.LyPro.BaseOpr;

import java.util.HashMap;

public class BaseOpr {
	protected HashMap<String,Object> CZYData = new HashMap<String,Object>();
	
	public Object PtyGet(String PtyName){
		return CZYData.get(PtyName) == null?"":CZYData.get(PtyName);
	}
	
	public String PtyGetString(String PtyName){
		return CheckString(PtyName);
	}
	
	public void setData(String FieldName,Object Value){
		CZYData.put(FieldName, Value);
	}
	
	protected String CheckString(String FileName){
		String value = (String) CZYData.get(FileName);
		return (value ==null || "null".equals(value)) ? "":value;
	}
	
	protected int CheckInt(String FileName){
		String value = String.valueOf(CZYData.get(FileName));
		return CZYData.get(FileName) == null?0:Integer.valueOf(value);
	}
	
	protected boolean CheckBoolean(String FileName){
		String value = String.valueOf(CZYData.get(FileName));
		return value == null? false : Boolean.valueOf(value);
	}
	
	public String getCGH(){
		return CheckString("VJOBNUM");
	}
	public String getCXM(){
		return CheckString("VNAME");
	}
	public String getCSRM(){
		return CheckString("VUSER");
	}
	public String getCSSPATH(){
		return CheckString("VgetCSSPATH");
	}
	public int getIRYLX(){
		return CheckInt("VLEVEL");
	}
	public String getCJGBM(){
		return CheckString("VascNum");
	}
	public String getCJGMC(){
		return CheckString("VascName");
	}
}
