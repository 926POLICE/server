package ro.ubb.catalog.web.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ro.ubb.catalog.core.model.Donation;
import ro.ubb.catalog.core.model.Donor;
import ro.ubb.catalog.core.service.*;
import ro.ubb.catalog.core.utils.Utilities;
import ro.ubb.catalog.web.converter.BloodConverter;
import ro.ubb.catalog.web.converter.DonationConverter;
import ro.ubb.catalog.web.converter.DonorConverter;
import ro.ubb.catalog.web.dto.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Created by radu.
 */

@RestController
public class DonorController
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

    private DonorConverter donorConverter = new DonorConverter();
    private DonationConverter donationConverter = new DonationConverter();
    private BloodConverter bloodConverter = new BloodConverter();

    @RequestMapping(value = "/donors", method = RequestMethod.POST)
    DonorDTO registerUser(@RequestBody Map<String, String> json)
    {
        // Don't forget the case where the username and password already exist !!

        log.trace("---- regiterUser entered ----");

        String username = json.get("username");
        String password = json.get("password");
        String name = json.get("name");
        String birthString = json.get("birthday");
        String residence = json.get("residence");
        Double latitude = Double.parseDouble(json.get("latitude"));
        Double longitude = Double.parseDouble(json.get("longitude"));
        Long birthday = Long.parseLong(birthString);

        // username-u ar trebui sa fie unic nu? I mean nu cred ca poti sa ai doi useri cu acelasi username da parole diferite
        List<Donor> donors = donorService.getAllDonors().stream().filter(donor->donor.getUsername()==username).collect(Collectors.toList());
        if(!donors.isEmpty())
            throw new RuntimeException("This username is already taken!");

        Donor createdDonor = donorService.createDonor(username,password,name,birthday,residence,"","",true,"",true,latitude,longitude);
        DonorDTO donorDTO1 = donorConverter.convertModelToDto(createdDonor);

        log.trace("---- regiterUser finished ----");

        return donorDTO1;
    }

    @RequestMapping(value = "/donors/history/{donorID}", method = RequestMethod.GET)
    List<DonationDTO> getAnalysisHistory(@RequestBody final Long DonorID)
    {
        log.trace("getAnalysisHistory entered!");

        List<Donation> donations = donationService.getAllDonations().stream().filter(d-> d.getDonor().getId().equals(DonorID)).collect(Collectors.toList());

        log.trace("getAnalysisHistory exited!");

        return new ArrayList<>(donationConverter.convertModelsToDtos(donations));
    }

    @RequestMapping(value = "/donors/nextDonation/{donorID}", method = RequestMethod.GET)
    String getNextDonation(@RequestBody final Long donorID)
    {
        log.trace("getNextDonation: donorID={}",donorID);

        Optional<Donor> donorOptional = donorService.findbyID(donorID);
        if(donorOptional.isPresent()) {
            String nextDonation = donorOptional.get().getNextDonation().toString();

            log.trace("getNextDonation: result={}",nextDonation);
            return nextDonation;
        }
        else
            throw new RuntimeException("Invalid donor ID!");
    }

    @RequestMapping(value = "/donors/bloodContainers/{donorID}", method = RequestMethod.GET)
    List<BloodDTO> getBloodJourney(@RequestBody final Long donorID)
    {
        log.trace("getBloodJourney: donorDI={}",donorID);

        List<Donation> donations = new ArrayList<>();
        List<BloodDTO> bloodContainers = new ArrayList<>();
        Optional<Donor> donorOptional = donorService.findbyID(donorID);
        if(donorOptional.isPresent())
        {
            donations = donationService.getAllDonations().stream().filter(d->d.getDonor().equals(donorOptional.get())).collect(Collectors.toList());
            final AtomicReference<List<Donation>> ref = new AtomicReference<>(donations);
            bloodContainers = bloodService.getAllBloods().stream().filter(b->ref.get().contains(b.getDonation())).map(b->bloodConverter.convertModelToDto(b)).collect(Collectors.toList());
        }

        log.trace("getBloodJourney: result={}",bloodContainers);
        return bloodContainers;
    }

    @RequestMapping(value = "/donors/{donorID}", method = RequestMethod.PUT)
    DonorDTO updatePersonalDetails(@PathVariable final Long  donorID, @RequestBody Map<String, String> json)
    {
        log.trace("---- updatePersonalDetails entered ----");

        Optional<Donor> donor = donorService.findbyID(donorID);

        log.trace(donor.toString());

        if(donor.isPresent())
        {
            String name = json.get("name");
            String birthString = json.get("birthday");
            String residence = json.get("residence");
            Double latitude = Double.parseDouble(json.get("latitude"));
            Double longitude = Double.parseDouble(json.get("longitude"));

            Long birthday = Long.parseLong(birthString);

            String address = donor.get().getAddress();
            String bloodType = donor.get().getBloodType();
            Boolean Rh = donor.get().getRh();
            String anticorps = donor.get().getAnticorps();
            Boolean eligibility = donor.get().getEligibility();
            Long nextDonation = donor.get().getNextDonation();
            String username = donor.get().getUsername();
            String password = donor.get().getPassword();

            Optional<Donor> donorOptional = donorService.updateDonor(donorID,name,birthday,residence,address,bloodType,Rh,anticorps,true,latitude,longitude,eligibility,nextDonation,username,password);

            Map<String, DonorDTO> result = new HashMap<>();
            donorOptional.ifPresent(donor1 -> result.put("donor", donorConverter.convertModelToDto(donor1)));

            log.trace("updatePersonalDetails: result={}", result);

            return result.get("donor");
        }
        else
            throw new RuntimeException("Invalid donor ID!");
    }

    @RequestMapping(value = "/donors/{donorID}", method = RequestMethod.GET)
    DonorDTO getPersonalDetails(@PathVariable final  Long donorID)
    {
        log.trace("getPersonalDetails: donorID={}",donorID);

        Optional<Donor> donorOptional = donorService.findbyID(donorID);
        if(donorOptional.isPresent()) {
            DonorDTO donorDTO = donorConverter.convertModelToDto(donorOptional.get());

            log.trace("getPersonalDetails: result={}",donorDTO.toString());
            return donorDTO;
        }
        else
            throw new RuntimeException("Invalid donor ID!");

    }

    @RequestMapping(value = "/donors/history/{donorID}", method = RequestMethod.PUT)
    String updateMedicalHistory(@PathVariable final Long donorID, @RequestBody final String newHistory)
    {
        log.trace("updateMedicalHistory: donorID={}, newHistory={}",donorID,newHistory);

        donorService.updateMedicalHistory(donorID,newHistory);

        log.trace("updateHistory: result={}",newHistory);

        return donorService.findbyID(donorID).get().getMedicalHistory();
    }

    @RequestMapping(value = "/donations", method = RequestMethod.POST)
    DonationDTO donate(@RequestBody Map<String, String> json)
    {
        log.trace("---- donate entered ----");

        // get the ID's in strings first.
        log.trace(json.get("donorID"));
        log.trace(json.get("patientID"));

        Long donorID = Long.parseLong(json.get("donorID"));
        Long patientTD;

        try
        {
            patientTD = Long.parseLong(json.get("patientID"));
        }
        catch (Exception e)
        {
            patientTD = null;
        }

        Donation donation = donationService.createDonation(donorID,patientTD,clinicService.getTheClinic().getId());

        log.trace("donate: rsult={}",donation);
        return donationConverter.convertModelToDto(donation);
    }
}