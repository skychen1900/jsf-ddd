/*
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *  Copyright © 2018 Yamashita,Takahiro
 */
package spec.annotation.presentation.controller;

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
