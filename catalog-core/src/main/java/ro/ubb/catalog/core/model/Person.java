package ro.ubb.catalog.core.model;

import lombok.*;
import org.springframework.data.util.Pair;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
public abstract class Person extends BaseEntity<Long> implements Serializable
{
    protected String name;
    protected String birthday;
    protected String residence;
    protected String bloodType;
    protected Boolean Rh;
    protected String anticorps;
    protected Boolean isDonor;
    protected Double latitude;
    protected Double longitude;


}
