package com.tracy.rediscache.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

@Component
public class TopN {
    @Autowired
    ReadWrite readWrite;
    @Autowired
    RedisTemplate<String,Object> redisTemplate;
    @Value("${redis.N}")
    int N;


    public void write(String keyword,Object obj) throws IllegalAccessException {
        //1 获取反射
        Class<?> c=obj.getClass();
        //2 将对象的属性和属性值存入hash，key为keyword_全限定类名的拼接
        String key=keyword+"_"+c.getName();
        for(Field field:c.getDeclaredFields()){
            String propertyName = field.getName();
            field.setAccessible(true);
            Object propertyValue = field.get(obj);
            redisTemplate.opsForHash().put(key,propertyName,propertyValue);
        }
        //3 将key加入ZSet
        redisTemplate.opsForZSet().addIfAbsent("TopN",key,0);
        redisTemplate.opsForZSet().incrementScore("TopN",key,1);
        //4 删除排行N之后的元素
        if(redisTemplate.opsForZSet().size("TopN")>N){
            redisTemplate.opsForZSet().popMin("TopN");
        }

    }
    public Object read(String key) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        if(Boolean.FALSE.equals(redisTemplate.hasKey(key)))return null;
        //1 获取类名
        Class<?> c=Class.forName(key.split("_")[1]);
        //2 创建一个对象
        Object obj=c.newInstance();
        //3 从redis中读取缓存，并给obj赋值
        for(Field field:c.getDeclaredFields()){
            String propertyName = field.getName();
            field.setAccessible(true);
            Object propertyValue = redisTemplate.opsForHash().get(key,propertyName);
            field.set(obj,propertyValue);
        }
        //4 将key加入ZSet
        redisTemplate.opsForZSet().addIfAbsent("TopN",key,0);
        redisTemplate.opsForZSet().incrementScore("TopN",key,1);
        //5 删除排行N之后的元素
        if(redisTemplate.opsForZSet().size("TopN")>N){
            redisTemplate.opsForZSet().popMin("TopN");
        }
        return obj;
    }
}
