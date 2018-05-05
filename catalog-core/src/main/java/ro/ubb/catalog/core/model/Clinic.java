package ro.ubb.catalog.core.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "donationClinics")
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
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

    @OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "clinic")
    private Set<Blood> unusableBloodStock = new HashSet<>();

    @OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "clinic")
    private Set<Blood> untestedBloodStock = new HashSet<>();
}
