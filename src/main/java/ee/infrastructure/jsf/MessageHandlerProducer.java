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
package ee.infrastructure.jsf;

import ddd.domain.validation.MessageHandler;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

/**
 * メッセージ出力する機能の依存性の注入をします.
 *
 * @author Yamashita,Takahiro
 */
@Named
@Dependent
public class MessageHandlerProducer {

    @Produces
    public MessageHandler getViewMessageHandler() {
        return new JsfMessageHandler();
    }

}
