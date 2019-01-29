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
 *  Copyright © 2019 Yamashita,Takahiro
 */
package exsample.jsf.infrastructure.datasource.user;

import base.jpa.NativeQueryReader;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Yamashita,Takahiro
 */
@ApplicationScoped
class UserIdGenerator {

    @PersistenceContext
    private EntityManager em;
    private final AtomicInteger id = new AtomicInteger();

    private NativeQueryReader nativeQueryReader;

    UserIdGenerator() {
    }

    @Inject
    UserIdGenerator(NativeQueryReader nativeQueryReader) {
        this.nativeQueryReader = nativeQueryReader;
    }

    @PostConstruct
    void init() {
        this.id.set((Integer) em.createNativeQuery(nativeQueryReader.read("UsersMaxId.sql")).getSingleResult());
    }

    int nextId() {
        return this.id.incrementAndGet();
    }
}
