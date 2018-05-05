package ro.ubb.catalog.core.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "donations")
@AllArgsConstructor
public class Donation extends BaseEntity<Long> implements Serializable
{
    @OneToOne(cascade = CascadeType.ALL)
    private Blood R; // field for Red Cells

    @OneToOne(cascade = CascadeType.ALL)
    private Blood P; // field for Plasma

    @OneToOne(cascade = CascadeType.ALL)
    private Blood T; // field for Thrombochytes

    @OneToOne(cascade = CascadeType.ALL)
    private Donor donor;

    @OneToOne(cascade = CascadeType.ALL)
    private Patient patient;

    private Boolean analysisResult;

    @ManyToOne
    private Clinic clinic;
}
