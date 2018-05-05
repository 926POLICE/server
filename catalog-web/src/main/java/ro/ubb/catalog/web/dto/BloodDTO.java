package ro.ubb.catalog.web.dto;

import lombok.*;

@AllArgsConstructor
@Setter
@Getter
@ToString
public class BloodDTO extends BaseDto
{
    private String collectionDate;
    private Float quantity ;
    private Integer state;
    private String type;
    private Integer shelfLife;
    private Long donationID;
    private Long clinicID;
}
