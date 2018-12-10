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
package ee.interceptor.scope.conversation;

import java.util.Objects;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import spec.interfaces.infrastructure.CurrentViewContext;
import spec.scope.conversation.IllegalConversationStartpathException;

/**
 * 会話スコープのライフサイクルを操作するクラスです.
 *
 * @author Yamashita,Takahiro
 */
@Named
@RequestScoped
public class ConversationLifecycleManager {

    private Conversation conversation;
    private CurrentViewContext context;

    public ConversationLifecycleManager() {
    }

    @Inject
    public ConversationLifecycleManager(Conversation conversation, CurrentViewContext context) {
        this.conversation = conversation;
        this.context = context;
    }

    /**
     * 会話スコープを開始します
     */
    public void startConversation() {
        if (this.conversation.isTransient() == false) {
            return;
        }

        if (context.currentViewId().endsWith("/index.xhtml") == false) {
            throw new IllegalConversationStartpathException();
        }

        this.conversation.begin();
        this.conversation.setTimeout(300000);

    }

    /**
     * 会話スコープを終了します.
     * <P>
     * 画面パスのフォルダが変更された場合、会話スコープを終了します.<br>
     * フォルダが変更されていない場合は、会話スコープを終了しません.
     *
     * @param currentViewId 現在の画面ＩＤ
     * @param resultViewId 遷移先の画面ＩＤ
     */
    public void endConversation(String currentViewId, String resultViewId) {
        if (shouldConversationEnd(currentViewId, resultViewId)) {
            this.conversation.end();
        }
    }

    /**
     * 会話スコープを終了します.
     */
    public void endConversation() {
        if (this.conversation.isTransient()) {
            return;
        }
        this.conversation.end();
    }

    /**
     * 会話スコープIDを返却します.
     *
     * @return 会話スコープID
     */
    public String conversationId() {
        return this.conversation.getId();
    }

    //
    boolean shouldConversationEnd(String currentViewId, String resultViewId) {
        if (this.conversation.isTransient()) {
            return false;
        }
        String startViewFolder = currentViewId.substring(0, currentViewId.lastIndexOf("/") + 1);
        String resultViewFolder = resultViewId.substring(0, resultViewId.lastIndexOf("/") + 1);

        if (Objects.equals(startViewFolder, resultViewFolder)) {
            return false;
        }
        return Objects.equals(resultViewFolder, "") == false;
    }

}
