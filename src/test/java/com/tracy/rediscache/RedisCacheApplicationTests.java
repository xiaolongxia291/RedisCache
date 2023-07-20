package com.tracy.rediscache;

import com.tracy.rediscache.aop.MyAspect;
import com.tracy.rediscache.cache.AccessLimit;
import com.tracy.rediscache.cache.ReadWrite;
import com.tracy.rediscache.cache.TopN;
import com.tracy.rediscache.controller.TestController;
import com.tracy.rediscache.entity.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Timer;
import java.util.TimerTask;

@SpringBootTest
class RedisCacheApplicationTests {
    @Autowired
    ReadWrite readWrite;
    @Autowired
    TopN topN;
    @Autowired
    AccessLimit accessLimit;
    @Autowired
    RedisTemplate<String,Object> redisTemplate;
    @Autowired
    MyAspect myAspect;
    @Autowired
    TestController testController;


    @Test
    void testReadWrite() throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        readWrite.write("tracy",new Student("tracy","female"),1000);
        System.out.println(readWrite.read("tracy_com.tracy.rediscache.entity.Student"));
    }

    @Test
    void testTopN() throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        topN.write("tracy1",new Student("tracy","female"));
        topN.write("tracy2",new Student("tracy","female"));
        topN.read("tracy1_com.tracy.rediscache.entity.Student");
        topN.read("tracy1_com.tracy.rediscache.entity.Student");
        topN.write("tracy3",new Student("tracy","female"));
        topN.write("tracy4",new Student("tracy","female"));
        topN.write("tracy5",new Student("tracy","female"));
        topN.write("tracy6",new Student("tracy","female"));
        //全部弹出
        while(redisTemplate.opsForZSet().size("TopN")!=0){
            System.out.println(redisTemplate.opsForZSet().popMax("TopN"));
        }

    }

    @Test
    void testAccessLimit(){
        for(int i=0;i<20;++i){
            System.out.println(accessLimit.accessLimit("/student"));
        }
    }

    @Test
    void testMyAspect(){
        for(int i=0;i<10;++i){
            testController.test1();
        }
        for(int i=0;i<10;++i){
            testController.test2();
        }
        for(int i=0;i<10;++i){
            testController.test3();
        }
    }

}
