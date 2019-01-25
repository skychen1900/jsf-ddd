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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.transaction.Transactional;

/**
 *
 * @author Yamashita,Takahiro
 */
@ApplicationScoped
@Transactional
@SuppressFBWarnings({"DMI_EMPTY_DB_PASSWORD"})
public class DatastoreInitializer {

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
                 + "  GENDER INTEGER NOT NULL,\n"
                 + "  VERSION INTEGER NOT NULL\n"
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

    private void init() throws SQLException {

        String prefixDml = "INSERT INTO USERS (ID,EMAIL,NAME,PHONE_NUMBER,DATE_OF_BIRTH,GENDER,VERSION) VALUES";

        try (Connection connection = DriverManager.getConnection("jdbc:h2:~/ddd", "sa", "")) {

            List<String> sqls = new ArrayList<>();
            sqls.add(prefixDml + "(1,'aaaaaa@example.com','ＡＡ　ＡＡ','03-1234-5678','1980-04-01',0,1); ");
            sqls.add(prefixDml + "(2,'bbbbbb@example.com','ＢＢ　ＢＢ','03-2345-6789','2000-05-01',1,1); ");
            sqls.add(prefixDml + "(3,'cccccc@example.com','ＣＣ　ＣＣ','03-3456-7890','1990-08-31',2,1); ");

            for (String sql : sqls) {
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.executeUpdate();
                }
            }
            connection.commit();
        }

    }

}
