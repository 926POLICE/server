package ro.ubb.catalog.core.model;

import lombok.*;
import org.springframework.data.util.Pair;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@MappedSuperclass
public abstract class Person extends BaseEntity<Long> implements Serializable
{
    protected String name;
    protected Long birthday;
    protected String residence;
    protected String address;
    protected String bloodType;
    protected Boolean Rh;
    protected String anticorps;
    protected Boolean isDonor;
    protected Double latitude;
    protected Double longitude;

    public boolean isCompatible(Person p)
    {
        return this.getBloodType()==p.getBloodType() && this.getRh() == p.getRh() && this.getAnticorps() == p.getAnticorps();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        if (!super.equals(o)) return false;
        Person person = (Person) o;
        return Objects.equals(name, person.name) &&
                Objects.equals(birthday, person.birthday) &&
                Objects.equals(residence, person.residence) &&
                Objects.equals(bloodType, person.bloodType) &&
                Objects.equals(Rh, person.Rh) &&
                Objects.equals(anticorps, person.anticorps) &&
                Objects.equals(isDonor, person.isDonor) &&
                Objects.equals(latitude, person.latitude) &&
                Objects.equals(longitude, person.longitude);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), name, birthday, residence, bloodType, Rh, anticorps, isDonor, latitude, longitude);
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", birthday='" + birthday + '\'' +
                ", residence='" + residence + '\'' +
                ", bloodType='" + bloodType + '\'' +
                ", Rh=" + Rh +
                ", anticorps='" + anticorps + '\'' +
                ", isDonor=" + isDonor +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
