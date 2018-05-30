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
@Builder
public class Donation extends BaseEntity<Long> implements Serializable {
    @OneToOne(fetch = FetchType.EAGER,optional = true)
    private Blood R; // field for Red Cells

    @OneToOne(fetch = FetchType.EAGER, optional = true)
    private Blood P; // field for Plasma

    @OneToOne(fetch = FetchType.EAGER,optional = true)
    private Blood T; // field for Thrombochytes

    private Boolean analysisResult;

    @OneToOne(fetch = FetchType.EAGER)
    private Donor donor;

    @OneToOne(fetch = FetchType.EAGER, optional = true)
    private Patient patient;

    @ManyToOne
    private Clinic clinic;

    @Override
    public String toString() {
        if(patient==null || R==null)
        return "Donation{" +
                "analysisResult=" + analysisResult +
                ", donor=" + donor.getId() +
                '}';
        else
            return "Donation{" +
                    "R=" + R.getId() +
                    ", P=" + P.getId() +
                    ", T=" + T.getId() +
                    ", analysisResult=" + analysisResult +
                    ", patient=" + patient.getId()+
                    ", donor=" + donor.getId() +
                    '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Donation)) return false;
        if (!super.equals(o)) return false;
        Donation donation = (Donation) o;
        if (R == null)
            return
                    Objects.equals(analysisResult, donation.analysisResult) &&
                            Objects.equals(donor.getId(), donation.donor.getId());
        else
            return Objects.equals(R.getId(), donation.R.getId()) &&
                    Objects.equals(P.getId(), donation.P.getId()) &&
                    Objects.equals(T.getId(), donation.T.getId()) &&
                    Objects.equals(analysisResult, donation.analysisResult) &&
                    Objects.equals(donor.getId(), donation.donor.getId());
    }

    @Override
    public int hashCode() {
        if (R == null)
            return Objects.hash(super.hashCode(), analysisResult, donor.getId());
        else
            return Objects.hash(super.hashCode(), R.getId(), P.getId(), T.getId(), analysisResult, donor.getId());
    }
}
