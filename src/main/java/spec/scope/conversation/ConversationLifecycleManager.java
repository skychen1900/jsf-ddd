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
 *  Copyright ? 2018 Yamashita,Takahiro
 */
package spec.scope.conversation;

/**
 *
 * @author Yamashita,Takahiro
 */
public interface ConversationLifecycleManager {

    /**
     * 会話スコープIDを返却します.
     *
     * @return 会話スコープID
     */
    String conversationId();

    /**
     * 会話スコープを終了します.
     * <P>
     * 画面パスのフォルダが変更された場合、会話スコープを終了します.<br>
     * フォルダが変更されていない場合は、会話スコープを終了しません.
     *
     * @param currentViewId 現在の画面ＩＤ
     * @param resultViewId 遷移先の画面ＩＤ
     */
    void endConversation(String currentViewId, String resultViewId);

    /**
     * 会話スコープを終了します.
     */
    void endConversation();

    /**
     * 会話スコープを開始します
     */
    void startConversation();

}
