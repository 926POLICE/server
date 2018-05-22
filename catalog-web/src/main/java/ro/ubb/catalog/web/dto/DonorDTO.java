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
    protected Long birthday;
    protected String residence;
    protected String address;
    protected String bloodType;
    protected Boolean Rh;
    protected String anticorps;
    protected Boolean isDonor;
    protected Double latitude;
    protected Double longitude;

    private Long nextDonation;
    private Boolean eligibility;
    private Boolean hasBeenNotified;
    private Boolean lastAnalysisResult;
}
