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
package ee.jsf.message.errorstyle;

import base.xhtml.error.ErrorStyle;
import ee.jsf.context.InputComponentScanner;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import spec.message.ClientIdMessages;
import spec.context.ClientIds;
import ee.jsf.context.ClientIdsImpl;

/**
 *
 * @author Yamashita,Takahiro
 */
@Named("errorStyle")
@RequestScoped
public class ErrorStyleImpl implements ErrorStyle {

    private InputComponentScanner inputComponentScanner;
    private ClientIds filterdClientIds;

    private String errorStyle;

    private ErrorStyleImpl() {
    }

    @Inject
    public ErrorStyleImpl(InputComponentScanner inputComponentScanner) {
        this.inputComponentScanner = inputComponentScanner;
    }

    @PostConstruct
    private void init() {
        this.errorStyle = "error";
        this.filterdClientIds = new ClientIdsImpl();
    }

    @Override
    public void set(ClientIdMessages clientidMessages) {
        Set<String> _clientIds = clientidMessages.getClientIds();
        this.filterdClientIds = this.inputComponentScanner.getClientIds().filter(_clientIds);
    }

    /**
     * 指定したIDの項目が検証不正だった場合に適用する styleClass を返却します.
     * <P>
     * xhtmlでのパラメータ指定時には、シングルクウォートで値を指定してください.
     *
     * @param id 対象となるコンポーネントのID（JSFのクライアントＩＤではありません）
     * @return 当該項目IDにエラーがない場合は 空文字を返却します.
     */
    @Override
    public String byId(String id) {
        return this.filterdClientIds.findFirstById(id).isPresent() ? errorStyle : "";

    }

}
