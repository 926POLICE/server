package ro.ubb.catalog.web.dto;

import lombok.*;

@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class BloodDTO extends BaseDto
{
    private Long collectiondate;
    private Float quantity;
    private Integer state;
    private String type;
    private Integer shelflife;
    private Boolean tested;
    private Boolean usable;
    private Long donationid;
    private Long clinicid;
}
