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
 *  Copyright © 2019 Yamashita,Takahiro
 */
package spec.logger;

/**
 * Loggerの名前を管理する定数クラスです.
 *
 * @author Yamashita,Takahiro
 */
public class LoggerName {

    public static final String ROOT_NAME = "root";

    /**
     * 予約語である{@code class} を使用することで クラス名で作成することを一般的とするLoggerの名前との衝突が起きないようにしています.
     */
    public static final String INTERCEPTOR_SUB_NAME = "class";

}
