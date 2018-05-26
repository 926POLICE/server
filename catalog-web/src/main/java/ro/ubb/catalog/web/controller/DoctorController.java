package ro.ubb.catalog.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ro.ubb.catalog.core.model.Blood;
import ro.ubb.catalog.core.model.Doctor;
import ro.ubb.catalog.core.model.Request;
import ro.ubb.catalog.core.service.*;
import ro.ubb.catalog.web.converter.BloodConverter;
import ro.ubb.catalog.web.converter.RequestConverter;
import ro.ubb.catalog.web.dto.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class DoctorController {

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

    private RequestConverter requestConverter = new RequestConverter();

    @RequestMapping(value = "/requests", method = RequestMethod.POST)
    RequestDTO newBloodRequest(@RequestBody Map<String, String> json)
    {
        log.trace("new Blood entered!");

        Long DoctorID=Long.parseLong(json.get("DoctorID"));
        Long PatientID=Long.parseLong(json.get("PatientID"));
        Float RQuantity=Float.parseFloat(json.get("RQuantity"));
        Float PQuantity=Float.parseFloat(json.get("PQuantity"));
        Float TQuantity=Float.parseFloat(json.get("TQuantity"));

        Integer priority=Integer.parseInt(json.get("Priority"));

        log.trace(DoctorID.toString());
        log.trace(PatientID.toString());
        log.trace(RQuantity.toString());

        Request request = requestService.createRequest(PatientID,DoctorID,RQuantity,PQuantity,TQuantity,priority,clinicService.getTheClinic().getId());

        log.trace("new Blood exited!");

        return requestConverter.convertModelToDto(request);
    }

    @RequestMapping(value = "/requests/doctors", method = RequestMethod.GET)
    Set<RequestDTO> getAllDoctorRequests(@RequestBody final Long DoctorID){
        List<Doctor> doctors=doctorService.getAllDoctors()
                .stream()
                .filter(doctor -> doctor.getId()==DoctorID)
                .collect(Collectors.toList());

        if(doctors.isEmpty())
            throw new RuntimeException("Doctor does not exist!");

        return requestService.getAllRequests().stream()
                .filter(request -> request.getDoctor().getId()==DoctorID)
                .map(request -> new RequestDTO(request.getPatient().getId(),request.getDoctor().getId(), request.getRQuantity(),request.getPQuantity(),request.getTQuantity(), request.getPriority(),request.getCompleted(), request.getClinic().getId()))
                .collect(Collectors.toSet());
    }


    @RequestMapping(value = "/requests/status", method = RequestMethod.GET)
    String checkRequestStatus(@RequestBody final Long PatientID)
    {
        List<Request> requests=requestService.getAllRequests().stream()
                .filter(request -> request.getPatient().getId()==PatientID)
                .collect(Collectors.toList());


        if(requests.isEmpty())
            return "No request sent for this patient";

        for(Request r:requests)
            return r.getCompleted().toString();
        return null;
    }
}
