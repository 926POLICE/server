package ro.ubb.catalog.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Setter
@Getter
@ToString
public class DonationDTO  extends BaseDto {
    private Long RBloodID; // field for Red Cells

    private Long PBloodID; // field for Plasma

    private Long TBloodID; // field for Thrombochytes

    private Long donorID;

    private Long patientID;

    private Boolean analysisResult;

    private Long clinicID;
}
