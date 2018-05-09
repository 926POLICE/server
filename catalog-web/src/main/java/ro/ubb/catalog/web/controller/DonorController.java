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

/**
 * Created by radu.
 */

@RestController
public class DonorController {

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

    @RequestMapping(value = "/loginDonor", method = RequestMethod.GET)
    Long getUser(@RequestBody Map<String, String> json)//
    // @RequestBody final String username,@RequestBody final String password)
    {
        return null;
    }

    @RequestMapping(value = "/donors", method = RequestMethod.POST)
    DonorDTO registerUser(@RequestBody Map<String, String> json)
    // @RequestBody final String username,@RequestBody final String password,@RequestBody final String name,@RequestBody final String birthday,@RequestBody final String residence,@RequestBody final Double latitude,@RequestBody final Double longitude
    {
        log.trace("YEE");
        log.trace(json.get("username"));
        Double val = Double.parseDouble(json.get("latitude"));
        log.trace(val.toString());

        return null;
    }

    @RequestMapping(value = "/donors/history/{donorID}", method = RequestMethod.GET)
    List<DonationDTO> getAnalysisHistory(@RequestBody final Long DonorID)
    {
        return null;
    }

    @RequestMapping(value = "/donors/nextDonation/{donorID}", method = RequestMethod.GET)
    String getNextDonation(@RequestBody final Long DonorID)
    {
        return null;
    }

    @RequestMapping(value = "/donors/bloodContainers/{donorID}", method = RequestMethod.GET)
    List<BloodDTO> getBloodJourney(@RequestBody final Long DonorID)
    {
        return null;
    }

    @RequestMapping(value = "/donors/{donorID}", method = RequestMethod.PUT)
    DonorDTO updatePersonalDetails(@RequestBody Map<String, String> json)
    //@PathVariable final  Long donorID, @RequestBody final String name,@RequestBody final String birthday,@RequestBody final String residence,@RequestBody final Double latitude,@RequestBody final Double longitude)
    {
        return null;
    }

    // Returns a DonorDTO (contains the donor's personal details).
    @RequestMapping(value = "/donors/{donorID}", method = RequestMethod.GET)
    DonorDTO getPersonalDetails(@PathVariable final  Long DonorID)
    {
        return null;
    }

    @RequestMapping(value = "/donors/history/{donorID}", method = RequestMethod.PUT)
    String updateMedicalHistory(@PathVariable final Long donorID, @RequestBody final String newHistory)
    {
        return null;
    }

    @RequestMapping(value = "/donations", method = RequestMethod.POST)
    DonationDTO donate(@RequestBody Map<String, String> json)
    //@RequestBody final DonorDTO donor, @RequestBody final @Nullable Long PatientID)
    {
        return null;
    }
}
