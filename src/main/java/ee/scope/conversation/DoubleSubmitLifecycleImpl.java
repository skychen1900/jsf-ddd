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
package ee.scope.conversation;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import spec.scope.conversation.DoubleSubmitLifecycle;
import spec.scope.conversation.DoubleSubmitState;

/**
 *
 * @author Yamashita,Takahiro
 */
@ConversationScoped
public class DoubleSubmitLifecycleImpl implements DoubleSubmitLifecycle {

    private static final long serialVersionUID = 1L;

    private DoubleSubmitState doubleSubmitState;

    @PostConstruct
    void init() {
        this.doubleSubmitState = DoubleSubmitState.INIT;
    }

    @Override
    public boolean isSubmitted() {
        return this.doubleSubmitState.isSubmitted();
    }

    @Override
    public void nextState() {
        this.doubleSubmitState = this.doubleSubmitState.nextState();
    }

}
