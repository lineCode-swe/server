import redis.clients.jedis.Jedis;

import java.util.Map;
public class ProvaRedis {
        public static void main(String a[]) {
            Jedis UnitRepositoryRedis = new Jedis("127.0.0.1");
            UnitRepositoryRedis.hset("1", "name","nome");
            UnitRepositoryRedis.hset("2", "name","prova2");
            UnitRepositoryRedis.hset("paolo", "name","paolo2");
            Map<String, String> value = UnitRepositoryRedis.hgetAll("1");
            System.out.println(value.toString());
            UnitRepositoryRedis.hset("1", "name","nuovonome");
            value = UnitRepositoryRedis.hgetAll("1");
            System.out.println(value.toString());
            boolean prova=true;
            String conv=Boolean.toString(prova);
            System.out.println(prova);
        }
}

