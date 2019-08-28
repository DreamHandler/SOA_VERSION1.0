package SOA.Util.Model;

import java.sql.ResultSet;

/**
 * 对象基类
 * 
 * @author QZY
 *
 */
public abstract class modeUtil {
	/**
	 * 获得String类型字段值
	 * 
	 * @param inRs
	 * @param inFd字段名
	 * @return
	 * @throws Exception
	 */
	public String GetRSString(ResultSet inRs, String inFd) throws Exception {
		String Value = "";
		Value = inRs.getString(inFd);
		return ((Value == null) || "null".equals(Value)) ? "" : Value;
	}

	/**
	 * 初始化数据
	 * 
	 * @param inResultSet
	 * @throws Exception
	 */
	public abstract void ReadByDb(ResultSet inResultSet) throws Exception;
}
