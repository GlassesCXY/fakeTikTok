package com.qiu.apifinal.aop;

import cn.dev33.satoken.stp.StpUtil;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.AfterThrowing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Aspect
@Component
public class MonitoringAspect {

    private static final Logger logger = LoggerFactory.getLogger(MonitoringAspect.class);

    // 定义一个切点，匹配所有 Controller 类的方法
    @Pointcut("execution(* com.qiu.apifinal.controller..*(..))")
    public void controllerMethods() {
    }

    // 环绕通知，记录方法执行时间和输入输出信息
    @Around("controllerMethods()")
    public Object monitorExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis(); // 开始时间

        // 获取当前登录用户ID
        String userId = "Anonymous"; // 默认值
        if (StpUtil.isLogin()) {
            userId = StpUtil.getLoginId().toString();
        }

        // 记录输入参数和用户ID
        logger.info("User ID: {}, Method {}.{} called with arguments: {}",
                userId,
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));

        Object result = null;
        try {
            result = joinPoint.proceed(); // 执行目标方法
        } catch (Throwable throwable) {
            // 记录异常信息
            logger.error("Method {}.{} thrown an exception: {}",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    throwable.getMessage());
            throw throwable;
        }

        long duration = System.currentTimeMillis() - start; // 计算耗时

        // 记录输出结果和耗时
        logger.info("Method {}.{} executed with result: {} and took: {} ms",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                result,
                duration);

        return result;
    }
}
