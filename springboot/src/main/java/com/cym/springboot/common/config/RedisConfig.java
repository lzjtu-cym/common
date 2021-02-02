package com.cym.springboot.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Description: Redis配置类
 * @Date: 2021/1/27 14:40
 * @Author: lyf
 * @Version: 1.0
 */

@Configuration
public class RedisConfig {

    //redisTemplate注入到Spring容器
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        Jackson2JsonRedisSerializer jacksonSeial = new Jackson2JsonRedisSerializer(Object.class);
        redisTemplate.setConnectionFactory(factory);
        //key序列化
        redisTemplate.setKeySerializer(jacksonSeial);
        //value序列化
        redisTemplate.setValueSerializer(jacksonSeial);
        //value hashmap序列化
        redisTemplate.setHashKeySerializer(jacksonSeial);
        //key hashmap序列化
        redisTemplate.setHashValueSerializer(jacksonSeial);
        return redisTemplate;
    }
}
