/*
 * Copyright(C) 2013 Sanyu Academy All rights reserved.
 *
 */
package core.annotation.application;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Stereotype;
import javax.inject.Named;

/**
 * このアノテーションは Serviceの コンポーネントを表すために使用します.
 *
 * @author Yamashita,Takahiro
 */
@Documented
@Stereotype
@Named
@RequestScoped
@Target(value = {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Service {

}
