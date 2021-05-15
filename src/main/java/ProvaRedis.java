import redis.clients.jedis.Jedis;

import java.util.Map;
public class ProvaRedis {
        public static void main(String a[]) {
            Jedis UnitRepositoryRedis = new Jedis("localhost");
            UnitRepositoryRedis.hset("1", "name","prova");
            Map<String, String> value = UnitRepositoryRedis.hgetAll("1");
            System.out.println(value.toString());
        }
}

