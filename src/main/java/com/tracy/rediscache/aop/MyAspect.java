package com.tracy.rediscache.aop;

import com.tracy.rediscache.cache.AccessLimit;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MyAspect {
    @Autowired
    AccessLimit accessLimit;

    @Before("execution(* com.tracy.rediscache.controller..*.*(..))")
    public void checkLimit(JoinPoint joinPoint) {
        String signature=joinPoint.getTarget().getClass().getName()+"."+joinPoint.getSignature().getName()+"()";
        if(!accessLimit.accessLimit(signature)){
            throw new SecurityException("达到了限流上限！");
        }
    }
}