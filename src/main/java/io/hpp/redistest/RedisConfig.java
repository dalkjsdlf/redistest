package io.hpp.redistest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@EnableRedisRepositories
@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisHost,redisPort);
    }

    @Bean(name = "biddingRedisTemplate")
    public RedisTemplate<String, Long> biddingRedisTemplate() {
        // RedisTemplate 는 제네릭 타입 K,V 설정 가능
        // 첫 번째는 레디스 key 에 해당하고, 두 번째는 레디스 value 에 해당함
        // 경매에 참여한 유저 아이디를 저장하기 때문에 value 를 Long 타입으로 설정
        RedisTemplate<String, Long> biddingRedisTemplate = new RedisTemplate<>();
        biddingRedisTemplate.setConnectionFactory(redisConnectionFactory());

        // key 와 value 값을 직렬화/역직렬화하는 RedisSerializer 구현체 설정
        biddingRedisTemplate.setKeySerializer(new StringRedisSerializer());
        biddingRedisTemplate.setValueSerializer(new GenericToStringSerializer<>(Long.class));

        return biddingRedisTemplate;
    }
}