package ru.idfedorov09.telegram.bot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import ru.idfedorov09.telegram.bot.data.model.RedisServerData;
import ru.idfedorov09.telegram.bot.util.RedisUtil;

@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.password:#{null}}")
    private String redisPassword;

    @Bean
    public RedisServerData redisServerData() {
        return new RedisServerData(
                redisPort,
                redisHost,
                redisPassword
        );
    }

    @Bean
    public Jedis jedis(RedisServerData redisServerData){
        return RedisUtil.INSTANCE.getConnection(redisServerData);
    }


}