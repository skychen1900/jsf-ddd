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

import ddd.domain.exception.UnexpectedApplicationException;
import java.util.List;
import java.util.Optional;

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
     * @throws UnexpectedApplicationException 対象Entityが存在しない場合
     */
    public default User persistedUser(User user) {
        return this.findByEmail(user).orElseThrow(() -> new UnexpectedApplicationException("user.doesnot.exist.findbyEmail"));
    }

    /**
     * IDを元に最新のEntityを取得します.
     *
     * @param user 最新を取得するEntity
     * @return 取得した最新のEntity
     * @throws UnexpectedApplicationException 対象Entityが存在しない場合
     */
    public default User registeredUser(User user) {
        return this.findById(user).orElseThrow(() -> new UnexpectedApplicationException("user.doesnot.exist.findbyid"));
    }

    public default boolean isNotExistSameEmail(User user) {
        return this.findByEmail(user).isPresent() == false;
    }

    public default boolean isNotExistSameEmailForUpdate(User user) {
        return this.findByEmail(user)
                .map(et -> et.getUserId().equals(user.getUserId()))
                .orElse(true);
    }

    public Optional<User> findById(User user);

    public Optional<User> findByEmail(User user);

    public void register(User user);

    public void remove(User user);

}
