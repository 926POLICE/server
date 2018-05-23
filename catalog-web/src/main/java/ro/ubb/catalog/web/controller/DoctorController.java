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

    @RequestMapping(value = "/requests", method = RequestMethod.POST)
    RequestDTO newBloodRequest(@RequestBody Map<String, String> json)
    {
        Long DoctorID=Long.parseLong(json.get("DoctorID"));
        Long PatientID=Long.parseLong(json.get("PatientID"));
        Float RQuantity=Float.parseFloat(json.get("Rquantity"));
        Float PQuantity=Float.parseFloat(json.get("PQuantity"));
        Float TQuantity=Float.parseFloat(json.get("TQuantity"));

        Integer priority=Integer.parseInt(json.get("priority"));

        RequestDTO req=new RequestDTO(PatientID,DoctorID,RQuantity,PQuantity,TQuantity,priority,Boolean.FALSE,clinicService.getTheClinic().getId());
        return req;
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
        return null;
    }
}
