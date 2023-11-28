package com.lhclike.myblog.config;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


import java.util.Arrays;


@Configuration
public class RedisConfig extends CachingConfigurerSupport {

    @Bean
    public RedisTemplate<String, Object>  redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object>  template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
//		Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        //对象的序列化，GenericJackson2JsonRedisSerializer实现了RedisSerializer接口
        // 使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());

        // 使用GenericJackson2JsonRedisSerializer来序列化和反序列化redis的value值
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
//        template.setDefaultSerializer(serializer);
        return template;
    }

    /**
     * 重写key的生成策略，用【类名+方法名+参数名】这样就可以保证key不为空
     * @return
     */
    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, objects) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName()).append(".").append(method.getName()).append(Arrays.toString(objects));
            return sb.toString();
        };
    }
}