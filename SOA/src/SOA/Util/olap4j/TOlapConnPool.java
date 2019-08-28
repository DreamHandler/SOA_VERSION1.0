package SOA.Util.olap4j;

import org.apache.commons.pool.BaseKeyedPoolableObjectFactory;
import org.apache.commons.pool.KeyedObjectPool;
import org.apache.commons.pool.KeyedObjectPoolFactory;
import org.apache.commons.pool.KeyedPoolableObjectFactory;
import org.apache.commons.pool.impl.GenericKeyedObjectPool;
import org.apache.commons.pool.impl.GenericKeyedObjectPoolFactory;
import org.olap4j.OlapConnection;
import org.olap4j.OlapWrapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: QZY
 * \* Date: 2019/8/27
 * \* Time: 16:29
 * \
 */
public class TOlapConnPool {
    private static KeyedObjectPool OlapPool;
    static{
        try {
            setupObjectPool(); //对象池
        } catch (Exception e) {
        }
    }
    //创建对象池
    public static void setupObjectPool() throws Exception{
        KeyedPoolableObjectFactory factory = new ObjectFactory();
        KeyedObjectPoolFactory poolFactory =
                new GenericKeyedObjectPoolFactory(factory,
                        100,
                        GenericKeyedObjectPool.WHEN_EXHAUSTED_BLOCK,
                        100,
                        100,
                        10,
                        false,
                        false,
                        60*1000,
                        -10,
                        1000*60*60,
                        false);
        OlapPool = poolFactory.createPool();
    }
    /**
     * 创建一个新连接
     * @param ConnNO
     * @return
     * @throws Exception
     */
    public static OlapConnection createOlapConnection(String ConnNO) throws Exception {
        Properties pConfig = ReadOlapConfig.getConfig(ConnNO);
        if(pConfig == null){
            throw new Exception("在连接表(BASEMENT..TBZDBIDS)中未查询到数据，请确认是否已经配置了此连接。");
        }
        //加载驱动程序
        Class.forName(pConfig.getProperty("Class"));
        Properties info = new Properties();
        info.setProperty("cacheSize", "1000");
        info.put("user", pConfig.getProperty("user"));
        info.put("password", pConfig.getProperty("password"));
        //连接到多维数据集
        Connection connection = DriverManager.getConnection(pConfig.getProperty("Driver"), info);
        OlapWrapper wrapper = (OlapWrapper) connection;
        OlapConnection OlapConn= wrapper.unwrap(OlapConnection.class);
        return OlapConn;
    }
    /**
     * 获得数据连接
     * @return
     * @throws Exception
     */
    public static OlapConnection getOlapConnection(String ConnNo) throws Exception {
        return (OlapConnection)OlapPool.borrowObject(ConnNo);
    }
    /**
     * 返回数据连接
     * @param ConnNo
     * @throws Exception
     */
    public static void returnOlapConnection(String ConnNo,OlapConnection oConn) throws Exception {
        OlapPool.returnObject(ConnNo,oConn);
    }
}
/*
 * 对象生成工厂
 * 产生连接。
 */
class ObjectFactory extends BaseKeyedPoolableObjectFactory {

    public Object makeObject(Object ConnNO) throws Exception {
        return TOlapConnPool.createOlapConnection((String)ConnNO);
    }
}
