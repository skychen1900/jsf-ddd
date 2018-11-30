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
package spec.exception;

/**
 * {@code ExceptionHandlerWrapper} の具象クラスにて 例外に関する操作を行う際に発行する独自の実行時例外です.
 * <p>
 * 主にエラー画面への遷移へ誘導するために使用します。
 *
 * @author Yamashita,Takahiro
 */
public class ThrowableHandlerException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ThrowableHandlerException() {
    }

    public ThrowableHandlerException(String message) {
        super(message);
    }

    public ThrowableHandlerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ThrowableHandlerException(Throwable cause) {
        super(cause);
    }

    public ThrowableHandlerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
