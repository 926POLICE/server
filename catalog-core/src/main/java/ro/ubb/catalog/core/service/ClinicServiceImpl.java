package ro.ubb.catalog.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ro.ubb.catalog.core.model.Clinic;
import ro.ubb.catalog.core.repository.ClinicRepository;

import java.util.List;

public class ClinicServiceImpl implements ClinicService
{
    private static final Logger log = LoggerFactory.getLogger(DoctorServiceImpl.class);

    @Autowired
    private ClinicRepository clinicRepository;

    @Override
    public List<Clinic> getAllClinics() {
        log.trace("getAllDoctors --- method entered");

        List<Clinic> clinics = clinicRepository.findAll();

        log.trace("getAllDoctors: doctors={}", clinics);

        return clinics;
    }

    @Override
    public Clinic createClinic(Double latitude, Double longitude)
    {
        Clinic clinic = clinicRepository.save(new Clinic(latitude,longitude));
        return clinic;
    }

    @Override
    public void deleteClinic(Long id) {
        log.trace("deleteClinic: id={}", id);

        clinicRepository.deleteById(id);

        log.trace("deleteClinic --- method finished");
    }
}
