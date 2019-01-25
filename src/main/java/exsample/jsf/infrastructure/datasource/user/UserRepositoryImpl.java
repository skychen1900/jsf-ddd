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
package exsample.jsf.infrastructure.datasource.user;

import exsample.jsf.domain.model.user.DateOfBirth;
import exsample.jsf.domain.model.user.Gender;
import exsample.jsf.domain.model.user.PhoneNumber;
import exsample.jsf.domain.model.user.User;
import exsample.jsf.domain.model.user.UserEmail;
import exsample.jsf.domain.model.user.UserId;
import exsample.jsf.domain.model.user.UserName;
import exsample.jsf.domain.model.user.UserRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@RequestScoped
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private EntityManager em;

    private UserIdGenerator iDgenerator;

    UserRepositoryImpl() {
    }

    @Inject
    UserRepositoryImpl(UserIdGenerator iDgenerator) {
        this.iDgenerator = iDgenerator;
    }

    @Override
    public List<User> findAll() {
        List<Users> entities = em.createNamedQuery("Users.findAll", Users.class).getResultList();
        return entities.stream()
                .sorted(Comparator.comparing(entity -> entity.getId()))
                .map(this::toUser)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> findById(User user) {
        Users entity = em.find(Users.class, user.getUserId().getValue());
        return Optional.ofNullable(entity == null
                                   ? null
                                   : toUser(entity));
    }

    @Override
    public Optional<User> findByEmail(User user) {
        try {
            Users entity = em.createNamedQuery("Users.findByEmail", Users.class)
                    .setParameter("email", user.getUserEmail().getValue())
                    .getSingleResult();
            return Optional.of(toUser(entity));
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    @Override
    public void register(User user) {
        Users entity = user.getUserId().getValue() == null
                       ? new Users()
                       : em.find(Users.class, user.getUserId().getValue(), LockModeType.PESSIMISTIC_FORCE_INCREMENT);

        this.merge(entity, user);
        em.merge(entity);
    }

    @Override
    public void remove(User user) {
        Users entity = em.find(Users.class, user.getUserId().getValue(), LockModeType.PESSIMISTIC_FORCE_INCREMENT);
        em.remove(entity);
    }

    private User toUser(Users entity) {
        return new User(new UserId(entity.getId()),
                        new UserEmail(entity.getEmail()),
                        new UserName(entity.getUserName()),
                        new DateOfBirth(entity.getDateOfBirth()),
                        new PhoneNumber(entity.getPhoneNumber()),
                        new Gender(entity.getGender()));
    }

    private void merge(Users entity, User user) {
        entity.setId(toId(user.getUserId().getValue()));
        entity.setEmail(user.getUserEmail().getValue());
        entity.setUserName(user.getName().getValue());
        entity.setDateOfBirth(user.getDateOfBirth().date());
        entity.setPhoneNumber(user.getPhoneNumber().getValue());
        entity.setGender(user.getGender().getValue());
    }

    private Integer toId(Integer currentId) {
        return currentId != null
               ? currentId
               : iDgenerator.nextId();
    }
}
