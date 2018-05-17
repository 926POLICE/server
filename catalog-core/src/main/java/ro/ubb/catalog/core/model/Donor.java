package ro.ubb.catalog.core.model;

import lombok.*;
import org.springframework.data.util.Pair;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "donors")
public class Donor extends Person implements Serializable
{
    private String nextDonation;
    private Boolean eligibility;

    private String username;
    private String password;

    public Donor(String name, String birthday, String residence, String bloodType, Boolean Rh, String anticorps, Boolean isDonor, Double latitude, Double longitude, String username, String password) {
        super(name, birthday, residence, bloodType, Rh, anticorps, isDonor, latitude, longitude);
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "Donor{" +
                "nextDonation='" + nextDonation + '\'' +
                ", eligibility=" + eligibility +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
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
        if (!(o instanceof Donor)) return false;
        if (!super.equals(o)) return false;
        Donor donor = (Donor) o;
        return Objects.equals(nextDonation, donor.nextDonation) &&
                Objects.equals(eligibility, donor.eligibility) &&
                Objects.equals(username, donor.username) &&
                Objects.equals(password, donor.password);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), nextDonation, eligibility, username, password);
    }
}
