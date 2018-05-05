package ro.ubb.catalog.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.ubb.catalog.core.model.Clinic;
import ro.ubb.catalog.core.model.Doctor;
import ro.ubb.catalog.core.model.Patient;
import ro.ubb.catalog.core.model.Request;
import ro.ubb.catalog.core.repository.ClinicRepository;
import ro.ubb.catalog.core.repository.DoctorRepository;
import ro.ubb.catalog.core.repository.PatientRepository;
import ro.ubb.catalog.core.repository.RequestRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RequestServiceImpl implements RequestService
{
    private static final Logger log = LoggerFactory.getLogger(RequestServiceImpl.class);

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private ClinicRepository clinicRepository;

    @Override
    public List<Request> getAllRequests() {
        log.trace("getAllRequests --- method entered");

        List<Request> requests = requestRepository.findAll();

        log.trace("getAllRequests: requests={}", requests);

        return requests;
    }

    @Override
    public Request createRequest(Long patientID, Long doctorID, Float RQuantity, Float PQuantity, Float TQuantity, Boolean priority, Long clinicID) {

        Optional<Patient> patient = patientRepository.findById(patientID);
        Optional<Clinic> clinic = clinicRepository.findById(clinicID);
        Optional<Doctor> doctor = doctorRepository.findById(doctorID);
        if(doctor.isPresent()==false||patient.isPresent()==false||clinic.isPresent()==false)
            throw new RuntimeException("Invalid request constructor!");

        Request request = requestRepository.save(new Request(patient.get(),doctor.get(),RQuantity,PQuantity,TQuantity,priority,false,clinic.get()));
        return request;
    }

    @Override
    @Transactional
    public Optional<Request> updateRequest(Long requestID, Long patientID, Long doctorID, Float RQuantity, Float PQuantity, Float TQuantity, Boolean priority, Boolean completed, Long clinicID) {
        Optional<Request> optionalRequest = requestRepository.findById(requestID);

        Optional<Patient> patient = patientRepository.findById(patientID);
        Optional<Clinic> clinic = clinicRepository.findById(clinicID);
        Optional<Doctor> doctor = doctorRepository.findById(doctorID);
        if(doctor.isPresent()==false||patient.isPresent()==false||clinic.isPresent()==false)
            throw new RuntimeException("Invalid request update!");

        optionalRequest.ifPresent(st -> {
            st.setPatient(patient.get());
            st.setDoctor(doctor.get());
            st.setClinic(clinic.get());
            st.setCompleted(completed);
            st.setPriority(priority);
            st.setRQuantity(RQuantity);
            st.setPQuantity(PQuantity);
            st.setTQuantity(TQuantity);
        });

        return optionalRequest;
    }

    @Override
    public void deleteRequest(Long id) {
        log.trace("deleteRequest: id={}", id);

        requestRepository.deleteById(id);

        log.trace("deleteRequest --- method finished");
    }


}
