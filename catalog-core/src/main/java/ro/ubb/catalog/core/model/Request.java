package ro.ubb.catalog.core.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

@Entity
@Table(name = "requests")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
public class Request extends BaseEntity<Long> implements Serializable, Comparable<Request>
{
    private Float RQuantity;
    private Float PQuantity;
    private Float TQuantity;

    private Integer priority;
    private Boolean completed;

    @OneToOne(fetch = FetchType.EAGER)
    private Patient patient;

    @OneToOne(fetch = FetchType.EAGER)
    private Doctor doctor;

    private Long date;

    @ManyToOne
    private Clinic clinic;

    @Override
    public int compareTo(Request o)
    {
        if(!this.priority.equals(o.priority))
            return this.priority.compareTo(o.priority);
        else
            return this.getPatient().getIsDonor().compareTo(o.getPatient().getIsDonor());
    }

    @Override
    public String toString() {
        return "ID = " + super.getId() + " Request{" +
                "RQuantity=" + RQuantity +
                ", PQuantity=" + PQuantity +
                ", TQuantity=" + TQuantity +
                ", priority=" + priority +
                ", completed=" + completed +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Request)) return false;
        if (!super.equals(o)) return false;
        Request request = (Request) o;
        return Objects.equals(RQuantity, request.RQuantity) &&
                Objects.equals(PQuantity, request.PQuantity) &&
                Objects.equals(TQuantity, request.TQuantity) &&
                Objects.equals(priority, request.priority) &&
                Objects.equals(completed, request.completed) &&
                Objects.equals(patient.getId(), request.patient.getId()) &&
                Objects.equals(doctor.getId(), request.doctor.getId());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), RQuantity, PQuantity, TQuantity, priority, completed);
    }
}
