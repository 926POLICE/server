package ro.ubb.catalog.web.dto;

import lombok.*;

@AllArgsConstructor
@Setter
@Getter
@ToString
@NoArgsConstructor
public class DonorDTO  extends BaseDto
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

    private String nextDonation;
    private Boolean eligibility;
}
