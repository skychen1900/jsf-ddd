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
package ee.validation;

import java.util.Collections;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 * SortKeyとConstraintViolationのペアを扱うクラスです.
 *
 * @author Yamashita,Takahiro
 */
public class ConstraintViolationForMessages {

    private final List<ConstraintViolationForMessage> items;

    public ConstraintViolationForMessages(List<ConstraintViolationForMessage> constraintViolationForMessages) {
        this.items = constraintViolationForMessages;
    }

    public List<ConstraintViolationForMessage> list() {
        return Collections.unmodifiableList(items);
    }

    /**
     * 関数で情報を更新した新たなインスタンスを返却します.
     * <P>
     * 循環参照をさせないために関数型で呼出し元で処理します.
     *
     * @param unaryOperator 更新する関数
     * @return 更新した新たなインスタンス
     */
    public ConstraintViolationForMessages update(UnaryOperator<ConstraintViolationForMessage> unaryOperator) {
        return new ConstraintViolationForMessages(
                items.stream()
                        .map(c -> unaryOperator.apply(c))
                        .collect(Collectors.toList())
        );
    }

}
