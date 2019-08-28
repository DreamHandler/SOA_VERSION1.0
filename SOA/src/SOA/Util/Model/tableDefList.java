package SOA.Util.Model;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import SOA.Base.dataBase.JdbcBuilder;


public class tableDefList {
	private static HashMap<String, Table> FMapList = new HashMap<String, Table>();
	private static boolean BLoaded;
	
	public static synchronized HashMap<String, Table> getFMapList() {
		return FMapList;
	}

	public static boolean isBLoaded() {
		return BLoaded;
	}
	public void setBLoaded(boolean loaded) {
		BLoaded = loaded;
	}

	public tableDefList() throws Exception {
		super();
		LoadData();
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
			Table ATbDef = new Table();
			ATbDef.ReadByDb(inRs);
			FMapList.put(ATbDef.getVNO(), ATbDef);
		}
	}
	public static synchronized Table GetTableByVno(String VNO) throws Exception {
		return FMapList.get(VNO);
	}
	public static synchronized List<String> GetTables(String Tbbm, String inBeginDt,
			String EndDt) throws Exception {
		if(isBLoaded()){
			if(LoadData()){
				throw new Exception("TBALE初始化失败！");
			}
		}
		Table ATTbDef = FMapList.get(Tbbm);
		if(ATTbDef==null){
			throw new Exception("为找到编码为【"+Tbbm+"】的表数据！");
		}
		int I, N, IBeginY, IEndY, ITable, IBeginTb, IEndTb;
		String S = "";
		String BeginDt = inBeginDt;
		IBeginY = Integer.valueOf(BeginDt.substring(0, 4));
		IEndY = Integer.valueOf(EndDt.substring(0, 4));
		List<String> RList = new ArrayList<String>();
		switch (ATTbDef.ITYPE) {
			case 0: 	// 单表
				RList.add(ATTbDef.GetTbName(""));
				break;
			case 1 :  //单库年表
				for (I = IBeginY; I <= IEndY; I++) {
					RList.add(ATTbDef.VTABLENAME + ".." + ATTbDef.VDATABASE
							+ String.valueOf(I));
				}
				break;
			case 2 : //年库年表
				for (I = IBeginY; I <= IEndY; I++) {
					RList.add(ATTbDef.VTABLENAME+ String.valueOf(I) + ".." + ATTbDef.VDATABASE
							+ String.valueOf(I));
				}
				break;
			case 3 : //年库年月表
				int IBeginDM = Integer.valueOf(BeginDt.substring(5, 7));
				int IEndDM = Integer.valueOf(EndDt.substring(5, 7));
				
				S = IBeginDM < 10 ? "0" + String.valueOf(IBeginDM) : String.valueOf(IBeginDM); 
				IBeginTb = Integer.valueOf(String.valueOf(IBeginY) + S);
				
				S = IEndDM < 10 ? "0" + String.valueOf(IEndDM) : String.valueOf(IEndDM);
				IEndTb = Integer.valueOf(String.valueOf(IEndY) + S);
				for (I = IBeginY; I <= IEndY; I++) {
					for (N = 1; N < 13; N++) {
						S = N < 10 ? "0" + String.valueOf(N) : String.valueOf(N);
						ITable = Integer.valueOf(String.valueOf(I) + S);
						if (ITable >= IBeginTb && ITable <= IEndTb)
							RList.add(ATTbDef.VDATABASE +String.valueOf(I)+ ".." + ATTbDef.VTABLENAME  + String.valueOf(ITable));
			 		
					}
				}
				break;
		}
		return RList;
	}
}
