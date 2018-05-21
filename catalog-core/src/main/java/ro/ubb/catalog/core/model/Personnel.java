package ro.ubb.catalog.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "personnel")
@AllArgsConstructor
public class Personnel extends BaseEntity<Long>
{
    private String username;
    private String password;
}
