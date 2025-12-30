package org.sopt.snappinserver.global.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ServiceLoggingAspect {

    @Around("execution(* org.sopt.snappinserver..service..*(..))")
    public Object logService(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String signature = proceedingJoinPoint.getSignature().toShortString();
        long start = System.currentTimeMillis();

        try {
            Object result = proceedingJoinPoint.proceed();
            long took = System.currentTimeMillis() - start;

            log.info("{} success took={}ms", signature, took);

            return result;
        } catch (Throwable exception) {
            long took = System.currentTimeMillis() - start;

            log.error("{} failed took={}ms errorType={} message={}",
                signature, took, exception.getClass().getSimpleName(), exception.getMessage());

            throw exception;
        }
    }
}
