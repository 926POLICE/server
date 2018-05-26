package ro.ubb.catalog.core.model;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "doctors")
public class Doctor extends BaseEntity<Long> implements Serializable
{
    private String name;
    private String hospital;
    private String username;
    private String password;

    public Doctor(String name, String hospital, String username, String password) {
        this.name = name;
        this.hospital = hospital;
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "name='" + name + '\'' +
                ", hospital='" + hospital + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Doctor)) return false;
        if (!super.equals(o)) return false;
        Doctor doctor = (Doctor) o;
        return Objects.equals(name, doctor.name) &&
                Objects.equals(hospital, doctor.hospital) &&
                Objects.equals(username, doctor.username) &&
                Objects.equals(password, doctor.password);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), name, hospital, username, password);
    }
}
