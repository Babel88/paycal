package com.babel88.paycal.controllers.support.undo;

//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.After;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;

import org.springframework.stereotype.Component;

//@Aspect
@Component
public class UndoRedoAspect {
    private static UndoRedoAspect instance = new UndoRedoAspect();

    public static UndoRedoAspect getInstance() {
        return instance;
    }
//
//    private final Logger log = LoggerFactory.getLogger(this.getClass());
//
//    @Pointcut("execution(* com.babel88.paycal.*.*.*(..))")
//    public void paymentModelMethod() {
//    }
//
//    @After("paymentModelMethod() && @annotation(undoRedo)")
//    public Object processUndo(JoinPoint joinPoint, UndoRedo undoRedo) throws Throwable
//    {
//
//        log.debug("Payment model method execution intercepted...");
//
//        return 5;
//    }
}
