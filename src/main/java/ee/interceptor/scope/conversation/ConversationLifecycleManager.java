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

import spec.presentation.UrlContext;
import java.util.Objects;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Conversation;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * 会話スコープのライフサイクルを操作するクラスです.
 *
 * @author Yamashita,Takahiro
 */
@Named
@ApplicationScoped
public class ConversationLifecycleManager {

    private Conversation conversation;

    private UrlContext urlContext;

    public ConversationLifecycleManager() {
    }

    @Inject
    public ConversationLifecycleManager(Conversation conversation, UrlContext urlContext) {
        this.conversation = conversation;
        this.urlContext = urlContext;
    }

    /**
     * 会話スコープの開始します。
     * <P>
     * 会話スコープが未開始にもかかわらず、indexページ以外を遷移先として指定していた場合は、強制的にindexページへ遷移させます.
     *
     * @return 会話スコープ開始済みの場合は指定のページ、未開始の場合はindexページ
     */
    public String startAndForwardIndexPage() {
        String currentViewId = urlContext.currentViewId();
        if (this.conversation.isTransient() == false) {
            return currentViewId;
        }

        this.conversation.begin();
        this.conversation.setTimeout(300000);

        if (currentViewId.equals("index.xhtml") == false) {
            return (String) urlContext.responseViewId("index.xhtml");
        }
        return (String) urlContext.responseViewId(currentViewId);
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
        String startViewFolder = uriFolderPath(currentViewId);
        String resultViewFolder = uriFolderPath(resultViewId);

        if (Objects.equals(startViewFolder, resultViewFolder)) {
            return false;
        }
        return Objects.equals(resultViewFolder, "") == false;
    }

    //
    String uriFolderPath(String viewId) {
        String[] urlPaths = viewId.split("/");
        String viewItem = urlPaths[urlPaths.length - 1];
        int folderPathSize = viewId.length() - viewItem.length();
        return viewId.substring(0, folderPathSize);
    }

}
