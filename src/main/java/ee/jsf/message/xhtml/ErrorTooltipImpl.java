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
package ee.jsf.message.xhtml;

import base.xhtml.error.ErrorTooltip;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import ee.jsf.context.InputComponentScanner;
import java.util.Set;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import spec.message.ClientIdMessages;
import spec.message.validation.ClientIds;

/**
 *
 * @author Yamashita,Takahiro
 */
@Named("errorTooltip")
@RequestScoped
@SuppressFBWarnings("UWF_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR")
public class ErrorTooltipImpl implements ErrorTooltip {

    private InputComponentScanner inputComponentScanner;

    private ClientIds clientIds;
    private ClientIdMessages clientIdMessages;

    public ErrorTooltipImpl() {
    }

    @Inject
    public ErrorTooltipImpl(InputComponentScanner inputComponentScanner) {
        this.inputComponentScanner = inputComponentScanner;
    }

    @Override
    public void set(ClientIdMessages clientIdMessages) {
        Set<String> _clientIds = clientIdMessages.getClientIds();
        this.clientIds = inputComponentScanner.getClientIds().filter(_clientIds);
        this.clientIdMessages = clientIdMessages;
    }

    /**
     * 指定したIDの項目が検証不正だった場合に適用する メッセージ を返却します.
     * <P>
     * xhtmlでのパラメータ指定時には、シングルクウォートで値を指定してください.
     *
     * @param id 対象となるコンポーネントのID（JSFのクライアントＩＤではありません）
     * @return 当該項目IDにエラーがない場合は 空文字を返却します.
     */
    @Override
    public String byId(String id) {
        if (this.clientIds == null) {
            return "";
        }
        String clientId = this.clientIds.findFirstById(id).orElse("");
        return clientIdMessages.getMessage(clientId);
    }

    /**
     * 指定したClientId（フルパス）の項目が検証不正だった場合に適用する メッセージ を返却します.
     *
     * @param clientId 対象となるコンポーネントのID（JSFのクライアントＩＤではありません）
     * @return 当該項目IDにエラーがない場合は 空文字を返却します.
     */
    @Override
    public String byClientId(String clientId) {
        if (this.clientIdMessages == null) {
            return "";
        }
        return clientIdMessages.getMessage(clientId);
    }

}
