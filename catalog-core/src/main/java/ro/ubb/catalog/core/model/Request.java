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
public class Request extends BaseEntity<Long> implements Serializable, Comparable<Request>
{
    private Float RQuantity;
    private Float PQuantity;
    private Float TQuantity;

    private Integer priority;
    private Boolean completed;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Patient patient;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Doctor doctor;

    @ManyToOne
    private Clinic clinic;

    @Override
    public int compareTo(Request o)
    {
        if(this.patient.getIsDonor() != o.patient.getIsDonor())
            return this.patient.getIsDonor().compareTo(o.patient.getIsDonor());
        else
            return this.priority.compareTo(o.priority);
    }

    @Override
    public String toString() {
        return "Request{" +
                "RQuantity=" + RQuantity +
                ", PQuantity=" + PQuantity +
                ", TQuantity=" + TQuantity +
                ", priority=" + priority +
                ", completed=" + completed +
                ", patient=" + patient.getId() +
                ", doctor=" + doctor.getId() +
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
    public int hashCode() {

        return Objects.hash(super.hashCode(), RQuantity, PQuantity, TQuantity, priority, completed, patient.getId(), doctor.getId());
    }
}
