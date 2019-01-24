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

import exsample.jsf.domain.model.user.GenderType;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 *
 * @author Yamashita,Takahiro
 */
@Entity
@Table(name = "USERS")
@NamedQueries({
    @NamedQuery(name = "Users.findAll", query = "SELECT u FROM Users u"),
    @NamedQuery(name = "Users.findByEmail", query = "SELECT u FROM Users u WHERE u.email = :email")
})
class Users implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Integer id;

    private String email;

    @Column(name = "NAME")
    private String userName;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_OF_BIRTH")
    private Date dateOfBirth;

    private Integer gender;

    @Version
    private Integer version;

    Integer getId() {
        return id;
    }

    void setId(Integer id) {
        this.id = id;
    }

    String getEmail() {
        return email;
    }

    void setEmail(String email) {
        this.email = email;
    }

    String getUserName() {
        return userName;
    }

    void setUserName(String userName) {
        this.userName = userName;
    }

    String getPhoneNumber() {
        return phoneNumber;
    }

    void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    String getDateOfBirth() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(this.dateOfBirth);
    }

    void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = Date.from(dateOfBirth.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    GenderType getGender() {
        return GenderType.findBy(gender);
    }

    void setGender(GenderType genderType) {
        this.gender = genderType.getCode();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Users other = (Users) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "Users{" + "id=" + id + ", email=" + email + ", userName=" + userName + ", phoneNumber=" + phoneNumber + ", dateOfBirth=" + dateOfBirth + ", gender=" + gender + '}';
    }

}
