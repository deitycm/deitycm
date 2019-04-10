package cm.deity.base.memcahed;

public class MemcachedUtil {
    public static MemCachedClient memcachedClient;
    private static Logger LOGGER = LoggerFactory.getLogger(MemcachedUtil.class);

    static {
        if (memcachedClient == null) {
            Properties props = new Properties();
            try {
                props.load(MemcachedUtil.class.getClassLoader().getResourceAsStream("memcached.properties"));
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }

            memcachedClient = new MemCachedClient();
            memcachedClient.setDefaultEncoding("utf-8");

            // 初始化SockIOPool，管理memcached的连接池
            SockIOPool pool = SockIOPool.getInstance();

            // 设置缓存服务器列表，当使用分布式缓存的时，可以指定多个缓存服务器。（这里应该设置为多个不同的服务器）
            String serversObj = props.getProperty("memcache.server");
            String initConn = props.getProperty("memcache.initConn");
            String minConn = props.getProperty("memcache.minConn");
            String maxConn = props.getProperty("memcache.maxConn");
            String maintSleep = props.getProperty("memcache.maintSleep");
            boolean nagle = Boolean.valueOf(props.getProperty("memcache.nagle"));
            String socketTO = props.getProperty("memcache.socketTO");

            String[] servers = {String.valueOf(serversObj)};

            pool.setServers(servers);
            pool.setFailover(true);
            pool.setInitConn(Integer.valueOf(initConn)); // 设置初始连接
            pool.setMinConn(Integer.valueOf(minConn));// 设置最小连接
            pool.setMaxConn(Integer.valueOf(maxConn)); // 设置最大连接
            pool.setMaxIdle(1000 * 60 * 60 * 3); // 设置每个连接最大空闲时间3个小时
            pool.setMaintSleep(Integer.valueOf(maintSleep));
            pool.setNagle(nagle);
            pool.setSocketTO(Integer.valueOf(socketTO));
            pool.setAliveCheck(true);
            pool.initialize();
        }
    }

    public static Object get(String key) {
        return memcachedClient.get(key);
    }  
      
    public static boolean add(String key, Object value) {  
        return memcachedClient.add(key, value);
    }  
      
    public static boolean add(String key, Object value, Integer expire) {  
        return memcachedClient.add(key, value, expire);
    }  
      
    public static boolean set(String key, Object value) {  
        return memcachedClient.set(key, value);
    }  
      
    public static boolean set(String key, Object value, Integer expire) {  
        return memcachedClient.set(key, value, expire);
    }  
      
    public static boolean replace(String key, Object value) {  
        return memcachedClient.replace(key, value);
    }  
      
    public static boolean replace(String key, Object value, Integer expire) {  
        return memcachedClient.replace(key, value, expire);
    }  
      
    public static boolean delete(String key){  
        return memcachedClient.delete(key);
    }
}