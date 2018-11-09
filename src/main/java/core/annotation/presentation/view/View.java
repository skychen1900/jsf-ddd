/*
 * Copyright(C) 2013 Sanyu Academy All rights reserved.
 *
 */
package core.annotation.presentation.view;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Stereotype;
import javax.inject.Named;

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
public @interface View {

}
