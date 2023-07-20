package com.tracy.rediscache.config;
 
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
 
/**
 * 自定义一个redis template模板
 */
@Configuration
public class RedisConfig {
    @Bean
    @SuppressWarnings("all")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        //为了使开发方便，直接使用<String, Object>
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
 
        //Json序列化配置
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
 
        //string的序列化
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
 
        //key采用string的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        //hash的key也是用string的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        //value序列化方式采用jackson
        template.setValueSerializer(jackson2JsonRedisSerializer);
        //hash的value序列化方式采用jackson
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
 
        template.afterPropertiesSet();
 
        return template;
    }

//    @Bean
//    public RedisTemplate<String, String> redisTemplate2(RedisConnectionFactory factory) {
//        RedisTemplate<String, String> template = new RedisTemplate<>();
//        template.setConnectionFactory(factory);
//
//        //string的序列化
//        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
//
//        //key采用string的序列化方式
//        template.setKeySerializer(stringRedisSerializer);
//        //hash的key也是用string的序列化方式
//        template.setHashKeySerializer(stringRedisSerializer);
//
//        template.afterPropertiesSet();
//
//        return template;
//    }
}

