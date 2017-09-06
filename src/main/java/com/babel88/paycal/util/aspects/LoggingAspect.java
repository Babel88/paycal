package com.babel88.paycal.util.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * LoggingAspect aspect definition
 * Created by eddard on 5/23/17.
 */
//@Aspect
public class LoggingAspect {

    Logger log = LoggerFactory.getLogger("Log-Aspect");

    public LoggingAspect() {
    }

    @Pointcut("execution(* com.babel88.paycal.*.*(..))")
    public void selectAll(){}

    @Before("selectAll()")
    public void beforeAdvice(JoinPoint joinPoint){
        log.debug("Signature declaring type : " + joinPoint.getSignature().getDeclaringTypeName());
        log.debug("Signature name : " + joinPoint.getSignature().getName());
        log.debug("Arguments : " + Arrays.toString(joinPoint.getArgs()));
        log.debug("Target class : " + joinPoint.getTarget().getClass().getName());
    }

    /**
     * This method executes after selected method execution
     */
    @After("selectAll()")
    public void afterAdvice(JoinPoint joinPoint){
        log.debug(joinPoint.getSignature().getName()+"()"+" has been called");
    }

    /**
     * This is the method which I would like to execute
     * when any methods returns
     * @param retVal
     */
    @AfterReturning(pointcut = "selectAll()", returning = "retVal")
    public void afterReturningAdvice(JoinPoint joinPoint, Object retVal){
        log.debug("Exiting from Method :" + joinPoint.getSignature().getName());
        log.debug("Return value :" + retVal.toString());
    }

    /**
     * This is the method which will execute if there is an
     * exception raised by any method
     */
    @AfterThrowing(pointcut = "selectAll()", throwing = "e")
    public void AfterThrowingAdvice(JoinPoint joinPoint, Throwable e){
        log.error("An exception has been thrown in " + joinPoint.getSignature().getName() + "()");
        log.error("Cause :" + e.getCause());
    }

    @Around("selectAll()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable
    {
        log.debug("The method " + joinPoint.getSignature().getName() + "() begins with " + Arrays.toString(joinPoint.getArgs()));
        try
        {
            Object result = joinPoint.proceed();
            log.debug("The method " + joinPoint.getSignature().getName() + "() ends with " + result.toString());
            return result;
        }
        catch (IllegalArgumentException e)
        {
            log.error("Illegal argument " + Arrays.toString(joinPoint.getArgs()) + " in " + joinPoint.getSignature().getName() + "()");
            throw e;
        }
    }
}
