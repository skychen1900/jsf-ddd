/*
 * Copyright(C) 2013 Sanyu Academy All rights reserved.
 *
 */
package ee.domain.annotation.view;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Stereotype;
import javax.inject.Named;
import javax.interceptor.InterceptorBinding;

/**
 * このアノテーションは Viewの コンポーネントを表すために使用します.
 *
 * @author Yamashita,Takahiro
 */
@Documented
@Stereotype
@Named
@ConversationScoped
@Target(value = {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@InterceptorBinding
public @interface View {

}
