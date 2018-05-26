package ro.ubb.catalog.core.model;

import lombok.*;
import org.springframework.data.util.Pair;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "patients")
@AllArgsConstructor
public class Patient extends Person implements Serializable
{
    private String hospital;

    public Patient(String name, Long birthday, String residence, String address, String bloodType, Boolean Rh, String anticorps, Boolean isDonor, Double latitude, Double longitude, String hospital) {
        super(name, birthday, residence,address, bloodType, Rh, anticorps, isDonor, latitude, longitude);
        this.hospital = hospital;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "hospital='" + hospital + '\'' +
                ", name='" + name + '\'' +
                ", birthday='" + birthday + '\'' +
                ", residence='" + residence + '\'' +
                ", bloodType='" + bloodType + '\'' +
                ", Rh=" + Rh +
                ", anticorps='" + anticorps + '\'' +
                ", isDonor=" + isDonor +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patient)) return false;
        if (!super.equals(o)) return false;
        Patient patient = (Patient) o;
        return Objects.equals(hospital, patient.hospital);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), hospital);
    }
}
