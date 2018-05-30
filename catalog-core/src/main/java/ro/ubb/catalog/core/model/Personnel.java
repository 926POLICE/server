package ro.ubb.catalog.core.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "personnel")
@AllArgsConstructor
@Builder
public class Personnel extends BaseEntity<Long>
{
    private String username;
    private String password;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Personnel)) return false;
        if (!super.equals(o)) return false;
        Personnel personnel = (Personnel) o;
        return Objects.equals(username, personnel.username) &&
                Objects.equals(password, personnel.password);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), username, password);
    }
}
