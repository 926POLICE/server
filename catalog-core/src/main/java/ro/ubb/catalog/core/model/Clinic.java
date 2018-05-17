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
public class Clinic extends BaseEntity<Long> implements Serializable
{
    private Double latitude;
    private Double longitude;

    public Clinic(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @OneToMany(fetch= FetchType.EAGER,cascade = CascadeType.ALL, mappedBy = "clinic")
    private Set<Donation> donations = new HashSet<>();

    @OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "clinic")
    private Set<Request> requests  = new HashSet<>();

    @OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "clinic")
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
        return "Clinic{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
