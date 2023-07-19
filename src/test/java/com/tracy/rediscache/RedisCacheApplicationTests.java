package com.tracy.rediscache;

import com.tracy.rediscache.cache.ReadWrite;
import com.tracy.rediscache.entity.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RedisCacheApplicationTests {
    @Autowired
    ReadWrite readWrite;


    @Test
    void test() throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        readWrite.write("tracy",new Student("tracy","female"),1000);
        System.out.println(readWrite.read("tracy_com.tracy.rediscache.entity.Student"));
    }

}
