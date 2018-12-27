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
package spec.context;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 *
 * @author Yamashita,Takahiro
 */
public interface ClientIds {

    /**
     * フルパスのクライアントＩＤが存在するか判定します.
     *
     * @param clientId
     * @return {@code h:message} のクライアントＩＤが存在する場合 {@code true}
     */
    public boolean contains(String clientId);

    /**
     * 指定のフルパスのクライアントＩＤリストだけの新たなインスタンスを返却します.
     *
     * @param clientIdSet 取得対象となるクライアントＩＤリスト
     * @return 新たなインスタンス
     */
    public ClientIds filter(Set<String> clientIdSet);

    /**
     * 項目ＩＤが一致するクライアントＩＤを返却します.
     * <p>
     * クライアントＩＤは繰り返し領域を扱う場合を考慮し、複数保持していますが 本メソッドでは 先頭のクライアントＩＤを返却します.<br>
     * また、項目ＩＤが一致する要素が無い場合は {@code null}を返却します
     *
     * @param clientIds
     * @return 一致する項目ＩＤがあった場合は クライアントＩＤ（先頭）、無かった場合は {@code null}
     */
    public Optional<String> findFirstByClientId(ClientIds clientIds);

    /**
     * xhtmlに指定されているＩＤに合致する、フルパスのクライアントＩＤを返却します.
     *
     * @param id クライアントＩＤ（フルパスではない）
     * @return 指定のIDがxhtmlに指定されている場合はフルパスのクライアントＩＤ、存在しない場合は null を返却します.
     */
    public Optional<String> findFirstById(String id);

    /**
     * フルパスのクライアントＩＤリストを返却します
     *
     * @return フルパスのクライアントＩＤ
     */
    public Set<String> getClientIds();

    public void put(String id);

    public void put(String id, String clientId);

    public void put(String id, Set<String> clientIdSet);

    public void putAll(ClientIds clientIds);

    public Set<String> getClientIdSet();

    public Map<String, Set<String>> getClientIdMap();

}
