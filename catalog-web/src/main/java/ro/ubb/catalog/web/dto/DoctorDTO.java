package ro.ubb.catalog.web.dto;

import lombok.*;

@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class DoctorDTO  extends BaseDto
{
    private String name;
    private String hospital;
}
