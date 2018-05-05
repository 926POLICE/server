package ro.ubb.catalog.core.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "requests")
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class Request extends BaseEntity<Long> implements Serializable
{
    @OneToOne(cascade = CascadeType.ALL)
    private Patient patient;

    @OneToOne(cascade = CascadeType.ALL)
    private Doctor doctor;

    private Float RQuantity;
    private Float PQuantity;
    private Float TQuantity;

    private Boolean priority;
    private Boolean completed;

    @ManyToOne
    private Clinic clinic;
}
