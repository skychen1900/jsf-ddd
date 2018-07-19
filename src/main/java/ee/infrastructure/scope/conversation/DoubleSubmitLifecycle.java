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
package ee.infrastructure.scope.conversation;

import ee.domain.scope.conversation.DoubleSubmitState;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

/**
 *
 * @author Yamashita,Takahiro
 */
@Named
@ConversationScoped
public class DoubleSubmitLifecycle implements Serializable {

    private static final long serialVersionUID = 1L;

    private DoubleSubmitState doubleSubmitState;

    @PostConstruct
    void init() {
        this.doubleSubmitState = DoubleSubmitState.INIT;
    }

    public boolean isSubmitted() {
        return this.doubleSubmitState.isSubmitted();
    }

    public void nextState() {
        this.doubleSubmitState = this.doubleSubmitState.nextState();
    }

}
