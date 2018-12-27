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
package base.validation;

import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.METHOD;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import spec.validation.BeanValidationException;

/**
 * 自Service以外の事前条件、事後条件により検証を行いたい場合に指定するアノテーション.
 * <P>
 * 他Serviceの検証処理は、事前条件({@link ddd.application.commnand.CommandPreCondition#invalidPreCondition(java.lang.Object) })
 * ・事後条件({@link ddd.application.commnand.CommandPostCondition#invalidPostCondition(java.lang.Object) })を使用して呼び出します.
 * <br>
 * アノテーションを付与できるメソッドの戻り値の型は {@link ddd.domain.validation.ValidateCondition.Void} だけです.
 * <br>
 * また、メソッド名は BeanValidationの仕様により、プロパティ表記（（{@code getXxx} ）による実装が必須です.
 *
 *
 * @author Yamashita,Takahiro
 */
@Documented
@Constraint(validatedBy = {base.validation.ValidateCondition.Validator.class})
@Target({METHOD})
@Retention(RUNTIME)
public @interface ValidateCondition {

    String message() default "{}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({METHOD})
    @Retention(RUNTIME)
    @Documented
    public @interface List {

        ValidateCondition[] value();
    }

    public class Validator implements ConstraintValidator<ValidateCondition, Void> {

        @Override
        public void initialize(ValidateCondition value) {
        }

        @Override
        public boolean isValid(Void value, ConstraintValidatorContext context) {
            value.throwException();
            return true;
        }
    }

    /**
     * {@link base.validation.ValidateCondition} を付与するメソッドの戻り値の型
     */
    public static class Void {

        private BeanValidationException ex;

        public Void() {
        }

        public Void(BeanValidationException ex) {
            this.ex = ex;
        }

        void throwException() {
            if (ex != null) {
                throw ex;
            }
        }

    }

}
