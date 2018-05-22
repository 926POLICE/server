package ro.ubb.catalog.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Setter
@Getter
@ToString
public class PatientDTO  extends BaseDto {
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

    private String hospital;
}
