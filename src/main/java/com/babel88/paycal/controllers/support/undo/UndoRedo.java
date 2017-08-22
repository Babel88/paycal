package com.babel88.paycal.controllers.support.undo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UndoRedo {
    String value() default "";

    String key() default "";

    String condition() default "";
}
