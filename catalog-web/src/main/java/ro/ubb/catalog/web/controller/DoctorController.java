package ro.ubb.catalog.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ro.ubb.catalog.core.model.*;
import ro.ubb.catalog.core.repository.ClinicRepository;
import ro.ubb.catalog.core.repository.PersonnelRepository;
import ro.ubb.catalog.core.service.*;
import ro.ubb.catalog.web.converter.BloodConverter;
import ro.ubb.catalog.web.converter.RequestConverter;
import ro.ubb.catalog.web.dto.*;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class DoctorController
{
    private static final Logger log = LoggerFactory.getLogger(BloodController.class);

    @Autowired
    private BloodService bloodService;

    @Autowired
    private ClinicService clinicService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private DonorService donorService;

    @Autowired
    private DonationService donationService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private RequestService requestService;

    @Autowired
    private ClinicRepository clinicRepository;

    @Autowired
    private PersonnelRepository personnelRepository;

    private Long currentTime = Instant.now().getEpochSecond();

    private RequestConverter requestConverter = new RequestConverter();

    @RequestMapping(value = "/requests", method = RequestMethod.POST)
    public RequestDTO newBloodRequest(@RequestBody Map<String, String> json)
    {
        log.trace("new Blood entered!");

        log.trace(json.toString());

        Long DoctorID=Long.parseLong(json.get("doctorid"));
        Long PatientID=Long.parseLong(json.get("patientid"));
        Float RQuantity=Float.parseFloat(json.get("rquantity"));
        Float PQuantity=Float.parseFloat(json.get("pquantity"));
        Float TQuantity=Float.parseFloat(json.get("tquantity"));

        Integer priority=Integer.parseInt(json.get("priority"));

        log.trace(DoctorID.toString());
        log.trace(PatientID.toString());
        log.trace(RQuantity.toString());

        Request request = requestService.createRequest(PatientID,DoctorID,RQuantity,PQuantity,TQuantity,priority,clinicService.getTheClinic().getId());

        log.trace("new Blood exited!");

        return requestConverter.convertModelToDto(request);
    }

    @RequestMapping(value = "/requests/doctors", method = RequestMethod.GET)
    public List<RequestDTO> getAllDoctorRequests(@RequestBody final Long doctorid){
        List<Doctor> doctors=doctorService.getAllDoctors()
                .stream()
                .filter(doctor -> doctor.getId().equals(doctorid))
                .collect(Collectors.toList());

        if(doctors.isEmpty())
            throw new RuntimeException("Doctor does not exist!");

        return requestService.getAllRequests().stream()
                .filter(request -> request.getDoctor().getId().equals(doctorid))
                .map(request -> new RequestDTO(request.getPatient().getId(),request.getDoctor().getId(), request.getRQuantity(),request.getPQuantity(),request.getTQuantity(), request.getPriority(),request.getCompleted(), request.getClinic().getId()))
                .collect(Collectors.toList());
    }


    @RequestMapping(value = "/requests/status", method = RequestMethod.GET)
    public String checkRequestStatus(@RequestBody final Long patientid)
    {
        List<Request> requests=requestService.getAllRequests().stream()
                .filter(request -> request.getPatient().getId().equals(patientid))
                .collect(Collectors.toList());


        if(requests.isEmpty())
            return "No request sent for this patient";

        for(Request r:requests)
            return r.getCompleted().toString();
        return null;
    }

    public void addSampleData() throws Exception {
            Clinic clinic = clinicRepository.save(new Clinic(46.67f,23.50f));
            Doctor doctor=doctorService.createDoctor("dre","dre","Dr. Dre","central");
            Patient patient=patientService.createPatient("ionut",1l,"a","b","A",false,"none",false,1.0f,2.0f,"central");
            Donor donor= donorService.createDonor("donor","donor","ionut",1l,"a","b","A",false,"none",false,1.0f,2.0f);
            Donation donation = donationService.createDonation(donor.getId(),null,clinicService.getTheClinic().getId());
            Request request = requestService.createRequest(patient.getId(),doctor.getId(),1.0f,2.0f,3.0f,1,clinic.getId());
            Blood blood = bloodService.createBlood(currentTime,2.0f,1,"r",donation.getId(),clinic.getId());
            bloodService.testBlood(blood.getId(),true);
            personnelRepository.save(new Personnel("admin","admin"));
    }
}
