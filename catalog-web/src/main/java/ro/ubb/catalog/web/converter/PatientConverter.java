package ro.ubb.catalog.web.converter;

import ro.ubb.catalog.core.model.Patient;
import ro.ubb.catalog.web.dto.PatientDTO;

public class PatientConverter extends BaseConverter<Patient,PatientDTO> {
    @Override
    public Patient convertDtoToModel(PatientDTO dto) {
        throw new RuntimeException("Not implemented yet!");
    }

    @Override
    public PatientDTO convertModelToDto(Patient patient) {
        PatientDTO res = new PatientDTO(patient.getName(),patient.getBirthday(),patient.getResidence(),patient.getAddress(),patient.getBloodType(),patient.getRh(),patient.getAnticorps(),patient.getIsDonor(),patient.getLatitude(),patient.getLongitude(),patient.getHospital());

        res.setId(patient.getId());

        return res;
    }
}
