package ro.ubb.catalog.core.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "donationClinics")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
public class Clinic extends BaseEntity<Long> implements Serializable
{
    private Float latitude;
    private Float longitude;

    public Clinic(Float latitude, Float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @OneToMany(fetch= FetchType.EAGER, mappedBy = "clinic", orphanRemoval = true)
    private Set<Donation> donations = new HashSet<>();

    @OneToMany(fetch=FetchType.EAGER, mappedBy = "clinic", orphanRemoval = true)
    private Set<Request> requests  = new HashSet<>();

    @OneToMany(fetch=FetchType.EAGER, mappedBy = "clinic", orphanRemoval = true)
    private Set<Blood> bloodStock = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Clinic)) return false;
        if (!super.equals(o)) return false;
        Clinic clinic = (Clinic) o;
        return Objects.equals(latitude, clinic.latitude) &&
                Objects.equals(longitude, clinic.longitude);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), latitude, longitude);
    }

    @Override
    public String toString() {
        return "ID: " + super.getId()+" Clinic{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
