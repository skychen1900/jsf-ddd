/*
 * Copyright(C) 2013 Sanyu Academy All rights reserved.
 *
 */
package core.annotation.presentation.controller;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Controllor/Actionにて使用するViewコンテキストを表すために使用します.
 * <p>
 * 本Annotationで指定したフィールドのClass情報をViewコンテキストとして表明することで、
 * ActionとView（Form）の関連付けを明示的に行うために使用します.<br>
 * 例えば、Serviceの検証不正実行時例外の情報と Viewのフィールドを関連付けるために使用します.
 *
 * @author Yamashita,Takahiro
 */
@Documented
@Target(value = {ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewContext {

}
