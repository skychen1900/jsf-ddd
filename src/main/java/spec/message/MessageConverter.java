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
 *  Copyright ? 2018 Yamashita,Takahiro
 */
package spec.message;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import spec.message.validation.ClientidMessages;

/**
 *
 * @author Yamashita,Takahiro
 */
public interface MessageConverter {

    /**
     * 例外のメッセージ情報を元にリソースで表示用に編集したメッセージを返却します.
     *
     * @param message 変換前のメッセージ
     * @return 変換後のメッセージ
     */
    String toMessage(String message);

    /**
     * 検証結果から変換したメッセージを返却します.
     *
     * @param constraintViolations 検証結果
     * @return メッセージ
     */
    List<String> toMessages(Collection<ConstraintViolation<?>> constraintViolations);

    /**
     * メッセージ出力先ＩＤとメッセージの組み合わせ情報を返却します.
     *
     * @param constraintViolationSet 検証結果
     * @param actionClass 実行元であるActionクラス
     * @return メッセージ出力先ＩＤとメッセージの組み合わせ情報
     */
    public ClientidMessages toClientidMessages(Set<ConstraintViolation<?>> constraintViolationSet, Class<?> actionClass);

}
