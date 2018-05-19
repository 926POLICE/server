package ro.ubb.catalog.core.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "donations")
@AllArgsConstructor
public class Donation extends BaseEntity<Long> implements Serializable
{
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Blood R; // field for Red Cells

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Blood P; // field for Plasma

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Blood T; // field for Thrombochytes

    private Boolean analysisResult;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Donor donor;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Patient patient;

    @ManyToOne
    private Clinic clinic;

    @Override
    public String toString() {
        return "Donation{" +
                "R=" + R.getId() +
                ", P=" + P.getId() +
                ", T=" + T.getId() +
                ", analysisResult=" + analysisResult +
                ", donor=" + donor.getId() +
                ", patient=" + patient.getId() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Donation)) return false;
        if (!super.equals(o)) return false;
        Donation donation = (Donation) o;
        return Objects.equals(R.getId(), donation.R.getId()) &&
                Objects.equals(P.getId(), donation.P.getId()) &&
                Objects.equals(T.getId(), donation.T.getId()) &&
                Objects.equals(analysisResult, donation.analysisResult) &&
                Objects.equals(donor.getId(), donation.donor.getId()) &&
                Objects.equals(patient.getId(), donation.patient.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), R.getId(), P.getId(), T.getId(), analysisResult, donor.getId(), patient.getId());
    }
}
