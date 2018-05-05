package ro.ubb.catalog.core.model;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "doctors")
@AllArgsConstructor
public class Doctor extends BaseEntity<Long> implements Serializable
{
    private String name;
    private String hospital;
}
