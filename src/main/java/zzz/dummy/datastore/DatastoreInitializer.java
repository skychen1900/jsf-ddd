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
package zzz.dummy.datastore;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import exsample.jsf.domain.model.user.GenderType;
import exsample.jsf.infrastructure.datasource.user.Users;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 *
 * @author Yamashita,Takahiro
 */
@ApplicationScoped
@Transactional
public class DatastoreInitializer {

    @PersistenceContext
    EntityManager em;

    @SuppressFBWarnings({"DMI_EMPTY_DB_PASSWORD"})
    public void startUp(@Observes @Initialized(ApplicationScoped.class) Object event) throws SQLException, ClassNotFoundException {
        System.out.println(">> Startup:Initialize Dummy Datastore >>");
        this.createTable();
        this.init();
    }

    private void createTable() throws SQLException, ClassNotFoundException {

        Class.forName("org.h2.Driver");

        String sql
               = "DROP TABLE IF EXISTS PUBLIC.USERS;\n"
                 + "CREATE TABLE PUBLIC.USERS (\n"
                 + "  ID  INTEGER PRIMARY KEY,\n"
                 + "  EMAIL  VARCHAR(124) ,\n"
                 + "  NAME VARCHAR(40) NOT NULL,\n"
                 + "  PHONE_NUMBER VARCHAR(13) NOT NULL,\n"
                 + "  DATE_OF_BIRTH DATE NOT NULL,\n"
                 + "  GENDER INTEGER NOT NULL\n"
                 + ");\n"
                 + "\n"
                 + "COMMENT ON TABLE PUBLIC.USERS IS '利用者';\n"
                 + "COMMENT ON COLUMN PUBLIC.USERS.ID IS '利用者ID';\n"
                 + "COMMENT ON COLUMN PUBLIC.USERS.EMAIL IS '利用者メールアドレス';\n"
                 + "COMMENT ON COLUMN PUBLIC.USERS.NAME IS '利用者名';\n"
                 + "COMMENT ON COLUMN PUBLIC.USERS.PHONE_NUMBER IS '電話番号';\n"
                 + "COMMENT ON COLUMN PUBLIC.USERS.DATE_OF_BIRTH IS '生年月日';\n"
                 + "COMMENT ON COLUMN PUBLIC.USERS.GENDER IS '性別';";

        try (Connection connection = DriverManager.getConnection("jdbc:h2:~/ddd", "sa", "");
             PreparedStatement statement = connection.prepareStatement(sql);) {

            statement.execute();
            connection.commit();
        }

    }

    private void init() {
        Users users1 = new Users();
        users1.setId(1);
        users1.setEmail("aaaaaa@example.com");
        users1.setUserName("ＡＡ　ＡＡ");
        users1.setDateOfBirth(LocalDate.of(1980, 4, 1));
        users1.setPhoneNumber("03-1234-5678");
        users1.setGender(GenderType.MAN);
        em.persist(users1);

        Users users2 = new Users();
        users2.setId(2);
        users2.setEmail("bbbbbb@example.com");
        users2.setUserName("ＢＢ　ＢＢ");
        users2.setDateOfBirth(LocalDate.of(2000, 5, 1));
        users2.setPhoneNumber("03-2345-6789");
        users2.setGender(GenderType.WOMAN);
        em.persist(users2);

        Users users3 = new Users();
        users3.setId(3);
        users3.setEmail("cccccc@example.com");
        users3.setUserName("ＣＣ　ＣＣ");
        users3.setDateOfBirth(LocalDate.of(1990, 8, 31));
        users3.setPhoneNumber("03-3456-7890");
        users3.setGender(GenderType.OTHER);
        em.persist(users3);

    }

}
