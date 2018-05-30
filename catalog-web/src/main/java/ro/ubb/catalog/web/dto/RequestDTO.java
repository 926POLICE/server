package ro.ubb.catalog.web.dto;

import lombok.*;

@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class RequestDTO  extends BaseDto {
    private Long patientid;

    private Long doctorid;

    private Float rquantity;
    private Float pquantity;
    private Float tquantity;

    private Integer priority;
    private Boolean completed;

    private Long clinicid;
}
