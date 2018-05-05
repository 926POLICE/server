package ro.ubb.catalog.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Setter
@Getter
@ToString
public class RequestDTO  extends BaseDto {
    private Long patientID;

    private Long doctorID;

    private Float RQuantity;
    private Float PQuantity;
    private Float TQuantity;

    private Boolean priority;
    private Boolean completed;

    private Long clinicID;
}
