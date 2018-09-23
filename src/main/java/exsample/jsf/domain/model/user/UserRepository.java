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
package exsample.jsf.domain.model.user;

import java.util.List;

/**
 *
 * @author Yamashita,Takahiro
 */
public interface UserRepository {

    public List<User> findAll();

    /**
     * 検索キーを元に最新のEntityを取得します.
     *
     * @param user 最新を取得するEntity
     * @return 取得した最新のEntity
     * @throws NoExistsEntityException 対象Entityが存在しない場合
     */
    public User findByKey(User user);

    /**
     * IDを元に最新のEntityを取得します.
     *
     * @param user 最新を取得するEntity
     * @return 取得した最新のEntity
     * @throws NoExistsEntityException 対象Entityが存在しない場合
     */
    public User findById(User user);

    public boolean isExistById(User user);

    public boolean isExistByEmail(User user);

    public void register(User user);

    public void remove(User user);

}
