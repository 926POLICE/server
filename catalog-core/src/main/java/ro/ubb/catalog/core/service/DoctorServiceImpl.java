package ro.ubb.catalog.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.ubb.catalog.core.model.Doctor;
import ro.ubb.catalog.core.repository.DoctorRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorServiceImpl implements DoctorService
{
    private static final Logger log = LoggerFactory.getLogger(DoctorServiceImpl.class);

    @Autowired
    private DoctorRepository doctorRepository;

    @Override
    public List<Doctor> getAllDoctors() {
        log.trace("getAllDoctors --- method entered");

        List<Doctor> doctors = doctorRepository.findAll();

        log.trace("getAllDoctors: doctors={}", doctors);

        return doctors;
    }

    @Override
    public Doctor createDoctor(String name, String hospital) {
        Doctor doctor = doctorRepository.save(new Doctor(name,hospital));
        return doctor;
    }

    @Override
    @Transactional
    public Optional<Doctor> updateDoctor(Long doctorId, String name, String hospital) {
        Optional<Doctor> optionalDoctor = doctorRepository.findById(doctorId);

        optionalDoctor.ifPresent(st -> {
            st.setName(name);
            st.setHospital(hospital);
        });

        return optionalDoctor;
    }

    @Override
    public void deleteDoctor(Long id) {
        log.trace("deleteDoctor: id={}", id);

        doctorRepository.deleteById(id);

        log.trace("deleteDoctor --- method finished");
    }


}
