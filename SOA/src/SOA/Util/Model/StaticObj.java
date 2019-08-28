package SOA.Util.Model;

import java.text.SimpleDateFormat;

/**
 * 公共静态字段对象
 * @author Administrator
 *
 */
public class StaticObj {
	/**
	 * DOM对象字段说明父节点名称
	 */
	public static final String Dom_Explains = "Fields";
	/**
	 * DOM对象字段说明子节点名称
	 */
	public static final String Dom_Explain = "Field";
	/**
	 * DOM对象值节点父节点名称
	 */
	public static final String Dom_Values = "FieldsValue";
	/**
	 * DOM对象值节点子节点名称
	 */
	public static final String Dom_Value = "FieldValue";
	/**
	 *保存日志文件地址 
	 */
	public static final String LogPath = "C:\\AreaLog";
	/**
     * 回车
     */
	public static String Enter=Chr(13)+Chr(10);
	/**
	 * 成功消息
	 * @return
	 */
	public static final String RtnStrSaveSuccess() {
	    StringBuffer buffer = new StringBuffer();
	    buffer.append("<?xml version='1.0' encoding='UTF-8'?>");
	    buffer.append("<RES>");
	    buffer.append("<RES.2>1</RES.2>");
	    buffer.append("</RES>");
	    return buffer.toString();
	  }
	/**
	 * 返回对应值消息
	 * @param RtnStr
	 * @return
	 */
	  public static final String RtnStrValue(String RtnStr) {
	    StringBuffer buffer = new StringBuffer();
	    buffer.append("<?xml version='1.0' encoding='UTF-8'?>");
	    buffer.append("<RES>");
	    buffer.append("<DAT>" + RtnStr + "</DAT>");
	    buffer.append("</RES>");
	    return buffer.toString();
	  }
	  /**
	   * 失败消息
	   * @return
	   */
	  public static final String RtnStrFail() {
	    StringBuffer buffer = new StringBuffer();
	    buffer.append("<?xml version='1.0' encoding='UTF-8'?>");
	    buffer.append("<RES>");
	    buffer.append("<RES.2>0</RES.2>");
	    buffer.append("</RES>");
	    return buffer.toString();
	  }
	  /**
	   * 返回错误消息
	   * @param e
	   * @return
	   */
	  public static final String RtnExcFail(Exception e) {
		    StringBuffer buffer = new StringBuffer();
		    buffer.append("<?xml version='1.0' encoding='UTF-8'?>");
		    buffer.append("<RES>");
		    buffer.append("<RES.2>0</RES.2>");
		    buffer.append("<RES.3>"+e.getMessage()+"</RES.3>");
		    buffer.append("</RES>");
		    return buffer.toString();
	  }
	  /**
		 * 
		 * 将ASCII码转换成字符。
		 */
		public static String Chr(int I){
			return new String(String.valueOf((char)I));
		}
		/**
		 * 可传入默认格式"yyyy-MM-dd HH:mm:ss"
		 *类函数，判断日期型数据，返回日期型字符串。
		 */
		public String NowStr(String infmt){
			return new SimpleDateFormat(infmt).format(System.currentTimeMillis());
		}
}
