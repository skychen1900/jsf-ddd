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
package exsample.jsf.domain.model.user;

/**
 *
 * @author Yamashita,Takahiro
 */
public enum GenderType {
    MAN(0, "男性"),
    WOMAN(1, "女性"),
    OTHER(2, "その他");

    private final Integer code;
    private final String value;

    private GenderType(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public static GenderType findBy(Integer code) {
        for (GenderType type : GenderType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        throw new IllegalStateException("GenderType does not exist.");
    }

    public Integer getCode() {
        return this.code;
    }

    public String getValue() {
        return this.value;
    }

}
