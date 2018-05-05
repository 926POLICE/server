package ro.ubb.catalog.web.converter;

import ro.ubb.catalog.core.model.Doctor;
import ro.ubb.catalog.web.dto.DoctorDTO;

public class DoctorConverter extends BaseConverter<Doctor, DoctorDTO> {
    @Override
    public Doctor convertDtoToModel(DoctorDTO dto) {
        throw new RuntimeException("Not implemented yet!");
    }

    @Override
    public DoctorDTO convertModelToDto(Doctor doctor) {
        DoctorDTO res = new DoctorDTO(doctor.getName(),doctor.getHospital());
        res.setId(doctor.getId());
        return res;
    }
}
