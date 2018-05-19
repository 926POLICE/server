package ro.ubb.catalog.web.dto;

import lombok.*;

@AllArgsConstructor
@Setter
@Getter
@ToString
public class BloodDTO extends BaseDto
{
    private Long collectionDate;
    private Float quantity ;
    private Integer state;
    private String type;
    private Integer shelfLife;
    private Boolean tested;
    private Boolean usable;
    private Long donationID;
    private Long clinicID;
}
