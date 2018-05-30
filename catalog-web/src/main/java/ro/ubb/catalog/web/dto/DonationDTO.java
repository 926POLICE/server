package ro.ubb.catalog.web.dto;

import lombok.*;

@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class DonationDTO  extends BaseDto {
    private Long rbloodid; // field for Red Cells

    private Long pbloodid; // field for Plasma

    private Long tbloodid; // field for Thrombochytes

    private Long donorid;

    private Long patientid;

    private Boolean analysisresult;

    private Long clinicid;
}
