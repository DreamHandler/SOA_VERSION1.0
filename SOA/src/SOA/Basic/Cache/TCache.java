package SOA.Basic.Cache;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import SOA.LyPro.BaseOpr.BaseOpr;

public class TCache {
	private static HashMap<String,Object> DataList = new HashMap<String,Object>();
	
	public static synchronized BaseOpr getSession(String key){
		BaseOpr inOpr = null;
		if(DataList.get(key)instanceof HttpSession){
			HttpSession session = (HttpSession) DataList.get(key);
			try{
				inOpr = (BaseOpr) session.getAttribute("opr");
			}catch(Exception e){
				inOpr = null;
			}
		}else{
			inOpr = (BaseOpr) DataList.get(key);
		}
		return inOpr;
	}
	
	public static synchronized void setSession(String key,Object obj){
		DataList.put(key, obj);
	}
	
	public static synchronized void clear(String key){
		DataList.remove(key);
	}
}
