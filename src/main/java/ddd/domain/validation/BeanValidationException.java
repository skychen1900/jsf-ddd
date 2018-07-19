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
package ddd.domain.validation;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.Set;
import javax.validation.ConstraintViolation;

/**
 * 検証実行時例外
 *
 * @author Yamashita,Takahiro
 */
public class BeanValidationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final transient Set<ConstraintViolation<Object>> validatedResults;

    public BeanValidationException(Set<ConstraintViolation<Object>> validatedResults) {
        this.validatedResults = validatedResults;
    }

    /**
     * 検証結果を返却します.
     *
     * @return 検証結果
     */
    public Set<ConstraintViolation<Object>> getValidatedResults() {
        return Collections.unmodifiableSet(validatedResults);
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
    }
}
