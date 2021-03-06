package com.kohls.cpe.darkside.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@Aspect
public class AspectJ {


    @Before("@within(org.springframework.web.bind.annotation.RestController) && args(firstName,..)")
    public void beforeRestAction(JoinPoint joinPoint, String firstName) {
        System.out.println("here I am! " + Arrays.toString(joinPoint.getArgs()));
    }

    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    public Object aroundRestAction(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
    }

    @After("@within(org.springframework.web.bind.annotation.RestController)")
    public void afterRestAction(JoinPoint joinPoint) {
        System.out.println("here I am! " + Arrays.toString(joinPoint.getArgs()));
    }

}

