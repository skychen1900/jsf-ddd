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
package ddd.presentation;

/**
 * ValueObjectを包含した表示に関するにPresenterとなるクラス（Form）に使用するインターフェースです.
 *
 * @param <ValueObjectType> Formが包含するValueObjectの型
 * @author Yamashita,Takahiro
 */
public interface Form<ValueObjectType> {

    /**
     * 表示情報を返却します.
     *
     * @return 表示情報
     */
    default public String display() {
        return "";
    }

    /**
     * 包含しているValueObjectを返却します.
     *
     * @return 包含しているValueObject
     */
    public ValueObjectType getValue();
}
