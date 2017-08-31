package com.enosh.itchatService.dispatcher;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention (RUNTIME)
@Target({METHOD, TYPE})
public @interface KeyMethodMapping {
	String value() default "";
}
