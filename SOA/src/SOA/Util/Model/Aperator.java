package SOA.Util.Model;

import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;

/**
 * 操作员类
 * @author Administrator
 *
 */
public class Aperator extends Menu{
	private Map<String,String> mapDate = null;//列集合
	private Document dat = null;//当前访问消息
	
	public Aperator(){
		super();
		this.mapDate = new HashMap<String, String>();
	}
	public Document getDat() {
		return dat;
	}

	public void setDat(Document dat) {
		this.dat = dat;
	}
	public void setAperator(String file,String value){
		this.mapDate.put(file, value);
	}
	public String getAperator(String key){
		if(this.mapDate.containsKey(key)){
			return this.mapDate.get(key);
		}else{
			return "";
		}
	}
	/**
	 * 操作员工号
	 * @return
	 */
	public String  getVjobnum(){
		return this.getAperator("VJOBNUM");
	}
	/**
	 * 操作员登录名
	 * @return
	 */
	public String  getVuser(){
		return this.getAperator("VUSER");
	}
	/**
	 * 所属组
	 * @return
	 */
	public String  getVname(){
		return this.getAperator("VNAME");
	}
	/**
	 * 所属组名称
	 * @return
	 */
	public String  getVascname(){
		return this.getAperator("VascName");
	}
	
}
