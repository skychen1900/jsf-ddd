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

/**
 * 検証結果の表示順ソート時に発生した実行時例外
 *
 * @author Yamashita,Takahiro
 */
public class ConstraintViolationConverterException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ConstraintViolationConverterException() {
    }

    public ConstraintViolationConverterException(String message) {
        super(message);
    }

    public ConstraintViolationConverterException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConstraintViolationConverterException(Throwable cause) {
        super(cause);
    }

    public ConstraintViolationConverterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
