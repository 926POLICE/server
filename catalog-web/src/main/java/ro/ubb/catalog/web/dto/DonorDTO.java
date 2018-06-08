package ro.ubb.catalog.web.dto;

import lombok.*;

@AllArgsConstructor
@Setter
@Getter
@ToString
@NoArgsConstructor
@Builder
public class DonorDTO  extends BaseDto
{
    protected String name;
    protected Long birthday;
    protected String residence;
    protected String address;
    protected String bloodtype;
    protected Boolean rh;
    protected String anticorps;
    protected Boolean isdonor;
    protected Float latitude;
    protected Float longitude;

    private Long nextdonation;
    private Boolean eligibility;
    private Boolean hasbeennotified;
    private Boolean lastanalysisresult;
    private String medicalHistory;
}
