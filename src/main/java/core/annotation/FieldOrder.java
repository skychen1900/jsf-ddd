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
package core.annotation;

import static java.lang.annotation.ElementType.FIELD;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassのFieldの順序を指定します.
 * <p>
 * Javaの仕様としてFieldの順序は保証されないため、ClassのFiled順序を指定するためのAnnotationです.
 *
 * @author Yamashita,Takahiro
 */
@Target({FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldOrder {

    short value() default Short.MAX_VALUE;
}
