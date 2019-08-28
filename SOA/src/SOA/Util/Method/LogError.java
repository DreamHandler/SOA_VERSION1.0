package SOA.Util.Method;

import java.io.File;
import java.io.FileOutputStream;

import SOA.Util.Model.Aperator;
import SOA.Util.Model.StaticObj;

public class LogError extends StaticObj {

	/**
	 * 记录日志调用函数
	 * 
	 * @param e
	 * @param inOpr
	 * @return
	 */
	public static synchronized boolean WriteLog(Exception e, Aperator inOpr) {
		boolean bol = false;
		bol = new LogError().WriteLogFun(e, inOpr);
		return bol;
	}

	// 写日志
	public boolean WriteLogFun(Exception e, Aperator inOpr) {
		String LogPath = this.getLogFileName(inOpr);
		try {
			if (e != null) {
				if (inOpr != null) {
					StringBuffer Msg = new StringBuffer();
					StackTraceElement[] Stack = e.getStackTrace();
					Msg.append("Exception : " + e.toString() + Enter);
					for (int i = 0; i < Stack.length; i += 1) {
						Msg.append("   " + Stack[i].getClassName() + "." + Stack[i].getMethodName() + "("
								+ Stack[i].getFileName() + ":" + Stack[i].getLineNumber() + ")" + Enter);
					}
					this.WriteDebug(this.StrStr(Msg.toString(), ""), LogPath);
					Msg = null;
				}
			}
			if (e != null) {
				String Str = e.toString();
				if (Str.indexOf("******") > -1) {
					this.Write(Str.substring(Str.indexOf("***"), Str.length()), LogPath);
				} else {
					this.Write(this.StrStr(Str, e.getStackTrace()[0].getClassName()), LogPath);
				}
			} else {
				this.Write(this.StrStr("异常为空！", ""), LogPath);
			}
		} catch (Exception e1) {
			System.out.print("【" + this.NowStr("yyyy-MM-dd") + "】日志记录失败，记录人工号："
					+ (inOpr == null ? "System" : inOpr.getVjobnum()) + "，日志：" + e1.getMessage());
		}
		return true;
	}
	/**
	 * 获取日志文件名称
	 * @param inOpr
	 * @return
	 */
	public String getLogFileName(Aperator inOpr){
		if(inOpr == null){
			return "System";
		}else{
			//LogName
			return inOpr.getVuser()+"("+inOpr.getVjobnum()+")";
		}
	}
	private boolean WriteDebug(String Msg,String LogName) throws Exception{
		if(!IsDir(LogPath)) return false;
		if(!IsDir(LogPath+"\\"+LogName)) return false;
		WriterFile(Msg, LogPath+"\\"+LogName+"\\"+NowStr("yyyy-MM-dd")+"DebugLog.txt");
		return true;
	}
	public boolean Write(String Msg, String LogName) {
		String LogPath = "C:\\AreaLog";
		if (!IsDir(LogPath))
			return false;
		if (!IsDir(LogPath + "\\" + LogName))
			return false;
		WriterFile(Msg, LogPath + "\\" + LogName + "\\" + NowStr("yyyy-MM-dd") + "Log.txt");
		return true;
	}

	private boolean IsDir(String LogPath) {
		File dir = new File(LogPath);
		if (!dir.exists()) {
			dir.mkdir();
		}
		return true;
	}

	/**
	 * 
	 * 把内容写入文件。这种方式是用追加模式写入文件。
	 */
	private Boolean WriterFile(String cont, String Filepath) {
		try {
			FileOutputStream fin = new FileOutputStream(Filepath, true);
			fin.write(cont.getBytes());
			fin.close();
		} catch (Exception e) {
			System.out.println("日志记录失败！");
			return false;
		}
		return true;
	}

	private String StrStr(String msg, String fun) {
		String str = "**********************************【系统】**********************************" + Enter;
		str += "【日志开始：" + this.NowStr("yyyy-MM-dd HH:mm:ss") + "】  在函数" + fun + Enter;
		str += "   " + msg + Enter;
		str += "【日志结束：" + this.NowStr("yyyy-MM-dd HH:mm:ss") + "】  " + Enter;
		return str;
	}
}
