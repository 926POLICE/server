package ro.ubb.catalog.core.model;

import lombok.*;
import org.springframework.data.util.Pair;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "donors")
@AllArgsConstructor
public class Donor extends Person implements Serializable
{
    private String nextDonation;
    private Boolean eligibility;

    public Donor(String name, String birthday, String residence, String bloodType, Boolean Rh, String anticorps, Boolean isDonor, Double latitude, Double longitude) {
        super(name, birthday, residence, bloodType, Rh, anticorps, isDonor, latitude, longitude);
    }
}
