package ro.ubb.catalog.core.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "bloodContainers")
public class Blood extends BaseEntity<Long> implements Serializable
{
    private String collectionDate;
    private Float quantity ;
    private Integer state;
    private String type;
    private Integer shelfLife;

    @OneToOne
    private Donation donation;

    @ManyToOne
    private Clinic clinic;

    public Blood(String collectionDate, Float quantity, Integer state, String type, Donation donation, Clinic clinic)
    {
        this.collectionDate=collectionDate;
        this.quantity=quantity;
        this.state=state;
        this.type=type;
        this.donation=donation;
        this.clinic=clinic;

        if(type.equals("r"))
            shelfLife = 42;
        else if(type.equals("p"))
            shelfLife = 365;
        else if(type.equals("t"))
            shelfLife = 5;
        else
            throw new RuntimeException("Invalid constructor parameters!");
    }
}
