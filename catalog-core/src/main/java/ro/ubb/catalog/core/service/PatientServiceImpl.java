package ro.ubb.catalog.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.ubb.catalog.core.model.Patient;
import ro.ubb.catalog.core.repository.PatientRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService
{
    private static final Logger log = LoggerFactory.getLogger(PatientServiceImpl.class);

    @Autowired
    private PatientRepository patientRepository;

    @Override
    public List<Patient> getAllPatients() {
        log.trace("getAllPatients --- method entered");

        List<Patient> patients = patientRepository.findAll();

        log.trace("getAllPatients: patients={}", patients);

        return patients;
    }

    @Override
    public Patient createPatient(String name,Long birthday,String residence,String address,String bloodType,Boolean Rh,String anticorps,Boolean isPatient,Double latitude,Double longitude, String hospital) {
        Patient patient = patientRepository.save(new Patient(name,birthday,residence,address,bloodType,Rh,anticorps,isPatient,latitude,longitude,hospital));
        return patient;
    }

    @Override
    @Transactional
    public Optional<Patient> updatePatient(Long patientId, String name,Long birthday,String residence,String address,String bloodType,Boolean Rh,String anticorps,Boolean isDonor,Double latitude,Double longitude, String hospital) {
        Optional<Patient> optionalPatient = patientRepository.findById(patientId);

        optionalPatient.ifPresent(st -> {
            st.setName(name);
            st.setBirthday(birthday);
            st.setResidence(residence);
            st.setAddress(address);
            st.setBloodType(bloodType);
            st.setRh(Rh);
            st.setAnticorps(anticorps);
            st.setIsDonor(isDonor);
            st.setLatitude(latitude);
            st.setLongitude(longitude);
            st.setHospital(hospital);
        });

        return optionalPatient;
    }

    @Override
    public void deletePatient(Long id) {
        log.trace("deletePatient: id={}", id);

        patientRepository.deleteById(id);

        log.trace("deletePatient --- method finished");
    }

    @Override
    public Optional<Patient> findByID(Long id) {
        return patientRepository.findById(id);
    }


}
