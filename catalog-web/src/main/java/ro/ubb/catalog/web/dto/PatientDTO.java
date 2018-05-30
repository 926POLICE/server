package ro.ubb.catalog.web.dto;

import lombok.*;

@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class PatientDTO  extends BaseDto {
    protected String name;
    protected Long birthday;
    protected String residence;
    protected String address;
    protected String bloodtype;
    protected Boolean rh;
    protected String anticorps;
    protected Boolean isdonor;
    protected Double latitude;
    protected Double longitude;

    private String hospital;
}
