/*
 * Copyright(C) 2013 Sanyu Academy All rights reserved.
 *
 */
package ee.domain.annotation.controller;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.interceptor.InterceptorBinding;

/**
 * このアノテーションは Controllerの Actionを表すために使用します.
 *
 * @author Yamashita,Takahiro
 */
@Documented
@Target(value = {ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@InterceptorBinding
public @interface Action {

}
