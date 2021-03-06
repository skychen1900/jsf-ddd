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
package ee.jsf.context;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import spec.context.ClientIds;

/**
 * 入力要素に付与されているクライアントＩＤ（項目名とフルパス）を扱う機能を提供します.
 * <p>
 * フルパスは、繰り返し領域の場合もあるため 複数保持する構造になっています.<br>
 * TODO:まだ、繰り返し領域を考慮していません。<br>
 *
 * @author Yamashita,Takahiro
 */
public class ClientIdsImpl implements ClientIds {

    /**
     * key:Id, value:clientIdのlist
     */
    private final Map<String, Set<String>> clientIdMap;
    private final Set<String> clientIdSet;

    public ClientIdsImpl() {
        this.clientIdMap = new HashMap<>();
        this.clientIdSet = new HashSet<>();
    }

    @Override
    public void put(String id) {
        this.put(id, id);
    }

    @Override
    public void put(String id, String clientId) {
        Set<String> _clientIds = clientIdMap.getOrDefault(id, new HashSet<>());
        _clientIds.add(clientId);
        this.clientIdMap.put(id, _clientIds);
        this.clientIdSet.addAll(_clientIds);
    }

    @Override
    public void put(String id, Set<String> clientIdSet) {
        Set<String> _clientIds = this.clientIdMap.getOrDefault(id, new HashSet<>());
        _clientIds.addAll(clientIdSet);
        this.clientIdMap.put(id, _clientIds);
        this.clientIdSet.addAll(clientIdSet);
    }

    @Override
    public void putAll(ClientIds clientIds) {
        this.clientIdMap.putAll(clientIds.getClientIdMap());
        this.clientIdSet.addAll(clientIds.getClientIdSet());
    }

    /**
     * xhtmlに指定されているＩＤに合致する、フルパスのクライアントＩＤを返却します.
     *
     * @param id クライアントＩＤ（フルパスではない）
     * @return 指定のIDがxhtmlに指定されている場合はフルパスのクライアントＩＤ、存在しない場合は null を返却します.
     */
    @Override
    public Optional<String> findFirstById(String id) {
        Set<String> _clientIds = this.clientIdMap.getOrDefault(id, new HashSet<>());
        return _clientIds.isEmpty()
               ? Optional.empty()
               : Optional.of(_clientIds.iterator().next());
    }

    /**
     * 項目ＩＤが一致するクライアントＩＤを返却します.
     * <p>
     * クライアントＩＤは繰り返し領域を扱う場合を考慮し、複数保持していますが 本メソッドでは 先頭のクライアントＩＤを返却します.<br>
     * また、項目ＩＤが一致する要素が無い場合は {@code null}を返却します
     *
     * @param clientIds
     * @return 一致する項目ＩＤがあった場合は クライアントＩＤ（先頭）、無かった場合は {@code null}
     */
    @Override
    public Optional<String> findFirstByClientId(ClientIds clientIds) {
        String key = clientIds.getClientIdMap().entrySet().iterator().next().getKey();
        return this.findFirstById(key);
    }

    /**
     * フルパスのクライアントＩＤが存在するか判定します.
     *
     * @param clientId
     * @return {@code h:message} のクライアントＩＤが存在する場合 {@code true}
     */
    @Override
    public boolean contains(String clientId) {
        return this.clientIdSet.contains(clientId);
    }

    /**
     * フルパスのクライアントＩＤリストを返却します
     *
     * @return フルパスのクライアントＩＤ
     */
    @Override
    public Set<String> getClientIds() {
        return Collections.unmodifiableSet(clientIdSet);
    }

    /**
     * 指定のフルパスのクライアントＩＤリストだけの新たなインスタンスを返却します.
     *
     * @param clientIdSet 取得対象となるクライアントＩＤリスト
     * @return 新たなインスタンス
     */
    @Override
    public ClientIds filter(Set<String> clientIdSet) {
        ClientIdsImpl _clientIdsWithComponents = new ClientIdsImpl();

        this.clientIdMap.forEach((_id, _clientIds) -> {

            clientIdSet.forEach(_clientId -> {

                if (_clientIds.contains(_clientId)) {
                    _clientIdsWithComponents.put(_id, _clientIds);
                }

            });

        });

        return _clientIdsWithComponents;
    }

    @Override
    public Map<String, Set<String>> getClientIdMap() {
        return Collections.unmodifiableMap(clientIdMap);
    }

    @Override
    public Set<String> getClientIdSet() {
        return Collections.unmodifiableSet(clientIdSet);
    }

}
