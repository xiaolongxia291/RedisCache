package com.tracy.rediscache.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

@Component
public class ReadWrite {
    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    public void writeObject(String keyword, Object obj, int seconds) throws IllegalAccessException {
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
        //3 设置过期时间，传入为0表示永不过期
        if(seconds!=0)redisTemplate.expire(key,seconds, TimeUnit.SECONDS);
    }

    public Object readObject(String key) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
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
        return obj;
    }
}
