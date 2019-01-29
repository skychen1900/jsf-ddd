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
package base.annotation.domain;

import static java.lang.annotation.ElementType.FIELD;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * EntityのIdentifierへ指定するアノテーションです.
 * <P>
 * 実際の値は、本アノテーションで指定したクラスの {@link EntityIdentifierValue}を対象とします.
 *
 * @author Yamashita,Takahiro
 */
@Target({FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EntityIdentifier {
}
