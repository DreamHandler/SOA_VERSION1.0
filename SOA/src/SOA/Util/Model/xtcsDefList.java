package SOA.Util.Model;

import java.sql.ResultSet;
import java.util.HashMap;

import SOA.Base.dataBase.JdbcBuilder;

public class xtcsDefList {
	private static HashMap<String, Xtcs> FMapList = new HashMap<String, Xtcs>();
	private static boolean BLoaded;
	
	public static synchronized HashMap<String, Xtcs> getFMapList() {
		return FMapList;
	}

	public static boolean isBLoaded() {
		return BLoaded;
	}
	public void setBLoaded(boolean loaded) {
		BLoaded = loaded;
	}

	public xtcsDefList() throws Exception {
		super();
	}
	/**
	 * 初始化方法
	 * 
	 * @return
	 * @throws Exception
	 */
	public static boolean LoadData() throws Exception {
		boolean B = false;
		if (BLoaded)
			return true;
		ResultSet Rs = null;
		JdbcBuilder ADbOpr = new JdbcBuilder();
		try {
			Rs = ADbOpr.ExecQry("SELECT * FROM basement..TBLYTABELSINFO WITH(NOLOCK) WHERE BENABLE = 1",null);
			if (Rs == null) {
				return B;
			}
			ReadByDb(Rs);
		}finally {
			if (Rs != null)
				Rs.close();
			ADbOpr.ReturnConn();
			ADbOpr = null;
		}
		return B;
	}
	private static void ReadByDb(ResultSet inRs) throws Exception {
		FMapList.clear();
		while (inRs.next()) {
			Xtcs ATbDef = new Xtcs();
			ATbDef.ReadByDb(inRs);
			FMapList.put(ATbDef.getVNO(), ATbDef);
		}
	}
	public static synchronized Xtcs GetXtcsByVno(String VNO) throws Exception {
		return FMapList.get(VNO);
	}
	public static synchronized String GetSysXtcs(String CNBMC,String MRVALUE) throws Exception{
		if(isBLoaded()){
			if(LoadData()){
				throw new Exception("参数初始化失败");
			};
		}
		Xtcs cs = FMapList.get(CNBMC);
		String Val = cs == null?MRVALUE:cs.getVVALUE();
		return Val;
	}
}
