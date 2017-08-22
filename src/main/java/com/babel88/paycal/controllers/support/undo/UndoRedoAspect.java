package com.babel88.paycal.controllers.support.undo;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.jfree.util.Log;

@Aspect
public class UndoRedoAspect {

    @Pointcut("execution(* com.babel88.paycal.*.*(..))")
    public void paymentModelMethod() {
    }

    @After("paymentModelMethod() && @annotation(undoRedo)")
    public Object processUndo(ProceedingJoinPoint joinPoint, UndoRedo undoRedo) throws Throwable {

        Log.debug("Payment model method execution intercepted...");

        return 5;
    }
}
