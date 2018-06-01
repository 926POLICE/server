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
@Builder
@AllArgsConstructor
public class Donor extends Person implements Serializable
{
    private Long nextDonation;
    private Boolean eligibility;
    private Boolean hasBeenNotified;
    private Boolean lastAnalysisResult;
    private String medicalHistory;

    private String username;
    private String password;

    public Donor(String name, Long birthday, String residence, String address, String bloodType, Boolean Rh, String anticorps, Boolean isDonor, Float latitude, Float longitude, String username, String password) {
        super(name, birthday, residence, address, bloodType, Rh, anticorps, isDonor, latitude, longitude);
        this.username = username;
        this.password = password;
        this.medicalHistory="";
        lastAnalysisResult=true;
        hasBeenNotified=false;
        nextDonation=1l;
        eligibility=false;
    }

    @Override
    public String toString() {
        return "ID: " + super.getId()+ " Donor{" +
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
                Objects.equals(hasBeenNotified, donor.hasBeenNotified) &&
                Objects.equals(lastAnalysisResult, donor.lastAnalysisResult) &&
                Objects.equals(medicalHistory, donor.medicalHistory) &&
                Objects.equals(username, donor.username) &&
                Objects.equals(password, donor.password);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), nextDonation, eligibility, username, password);
    }
}
