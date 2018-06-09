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
    @OneToOne(fetch = FetchType.EAGER, optional = true)
    private Blood R; // field for Red Cells

    @OneToOne(fetch = FetchType.EAGER, optional = true)
    private Blood P; // field for Plasma

    @OneToOne(fetch = FetchType.EAGER, optional = true)
    private Blood T; // field for Thrombochytes

    private Boolean analysisResult;

    @OneToOne(fetch = FetchType.EAGER)
    private Donor donor;

    @OneToOne(fetch = FetchType.EAGER, optional = true)
    private Patient patient;

    @ManyToOne
    private Clinic clinic;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Donation)) return false;
        if (!super.equals(o)) return false;
        Donation donation = (Donation) o;
            return  Objects.equals(getId(), donation.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode());
    }
}
