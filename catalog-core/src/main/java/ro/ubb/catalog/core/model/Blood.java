package ro.ubb.catalog.core.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "bloodContainers")
public class Blood extends BaseEntity<Long> implements Serializable {
    private String collectionDate;
    private Float quantity;
    private Integer state;
    private String type;
    private Integer shelfLife;
    private Boolean tested;

    @OneToOne
    private Donation donation;

    @ManyToOne
    private Clinic clinic;

    public Blood(String collectionDate, Float quantity, Integer state, String type, Donation donation, Clinic clinic) {
        this.collectionDate = collectionDate;
        this.quantity = quantity;
        this.state = state;
        this.type = type;
        this.donation = donation;
        this.clinic = clinic;
        this.tested = false;

        if (type.equals("r"))
            shelfLife = 42;
        else if (type.equals("p"))
            shelfLife = 365;
        else if (type.equals("t"))
            shelfLife = 5;
        else
            throw new RuntimeException("Invalid constructor parameters!");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Blood)) return false;
        if (!super.equals(o)) return false;
        Blood blood = (Blood) o;
        return Objects.equals(collectionDate, blood.collectionDate) &&
                Objects.equals(quantity, blood.quantity) &&
                Objects.equals(state, blood.state) &&
                Objects.equals(type, blood.type) &&
                Objects.equals(shelfLife, blood.shelfLife) &&
                Objects.equals(tested, blood.tested) &&
                Objects.equals(donation.getId(), blood.donation.getId()) &&
                Objects.equals(clinic.getId(), blood.clinic.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), collectionDate, quantity, state, type, shelfLife, tested);
    }

    @Override
    public String toString() {
        return "Blood{" +
                "collectionDate='" + collectionDate + '\'' +
                ", quantity=" + quantity +
                ", state=" + state +
                ", type='" + type + '\'' +
                ", shelfLife=" + shelfLife +
                ", tested=" + tested +
                '}';
    }
}
