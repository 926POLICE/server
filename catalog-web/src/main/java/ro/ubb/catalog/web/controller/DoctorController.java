package ro.ubb.catalog.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ro.ubb.catalog.core.model.Blood;
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
        return null;
    }

    @RequestMapping(value = "/requests/status", method = RequestMethod.GET)
    String checkRequestStatus(@RequestBody final Long PatientID)
    {
        return null;
    }
}
