package ro.ubb.catalog.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.ubb.catalog.core.model.*;
import ro.ubb.catalog.core.service.*;
import ro.ubb.catalog.web.converter.*;
import ro.ubb.catalog.web.dto.*;

import javax.swing.text.html.Option;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by radu.
 */

@RestController
public class ClinicController
{
    // catalin branch

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

    @RequestMapping(value = "/bloodStocks", method = RequestMethod.GET)
    List<BloodDTO> getBloodStocks()
    {
        return null;
    }

    @RequestMapping(value = "/bloodStocksUntested", method = RequestMethod.GET)
    List<BloodDTO> getUntestedBloodStocks()
    {
        return null;
    }

    @RequestMapping(value = "/bloodStocksUnusable", method = RequestMethod.GET)
    List<BloodDTO> getUnusableBloodStocks()
    {
        return null;
    }

    @RequestMapping(value = "/patients", method = RequestMethod.GET)
    Set<PatientDTO> getPatients()
    {
        // lat/lng and math function to calculate the distance from a point x to a point y ;) (kudos to Prisacariu)
        List<Patient> patients = patientService.getAllPatients();
        PatientConverter p = new PatientConverter();
        return p.convertModelsToDtos(patients);
    }

    @RequestMapping(value = "/requests", method = RequestMethod.GET)
    Set<RequestDTO> getAllRequests()
    {
        List<Request> patients = requestService.getAllRequests();
        RequestConverter r = new RequestConverter();
        return r.convertModelsToDtos(patients);
    }

    @RequestMapping(value = "/pendingDonations", method = RequestMethod.GET)
    Set<DonationDTO> getAllPendingDonations()
    {
        List<Donation> donations = donationService.getAllDonations();
        donations = donations.stream().filter(d->d.getR()==null).collect(Collectors.toList());
        DonationConverter d = new DonationConverter();
        return d.convertModelsToDtos(donations);
    }

    @RequestMapping(value = "/bloodStocks/{bloodId}", method = RequestMethod.PUT)
    BloodDTO updateBlood(@PathVariable final Long bloodId, @RequestBody final BloodDTO bloodDto)
    {
        // ca in bloodController

        return null;
    }

    @RequestMapping(value = "/bloodStocksUntested/{bloodId}", method = RequestMethod.PUT)
    BloodDTO testBlood(@PathVariable final Long bloodId, @RequestBody final boolean flag)
    {
        // update din blood controller

        return null;
    }

    @RequestMapping(value = "/bloodStocksUnusable/{bloodId}", method = RequestMethod.DELETE)
    public ResponseEntity disposeBlood(@PathVariable final Long bloodId)
    {
        // delete din blood controller

        return null;
    }

    @RequestMapping(value = "/bloodStocks/{bloodId}", method = RequestMethod.DELETE)
    public ResponseEntity useBlood(@PathVariable final Long bloodId)
    {
        Clinic c = clinicService.getTheClinic();
        log.trace(c.getLatitude().toString());

        // simulated exception
        if(true)
            throw new RuntimeException("Testing exception handling!");

        return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.OK);
    }

    @RequestMapping(value = "/donors/eligibility/{donorID}", method = RequestMethod.PUT)
    DonorDTO setEligibility(@PathVariable final Long donorID, @RequestBody final boolean flag)
    {
        Donor old_donor = donorService.findbyID(donorID).get();
        DonorConverter d = new DonorConverter();
        return d.convertModelToDto(donorService.updateDonor(donorID,old_donor.getName(),old_donor.getBirthday(),old_donor.getResidence(),old_donor.getBloodType(),old_donor.getRh(),old_donor.getAnticorps(),old_donor.getIsDonor(),old_donor.getLatitude(),old_donor.getLongitude(),flag,old_donor.getNextDonation(),old_donor.getUsername(),old_donor.getPassword()).get());
    }

    @RequestMapping(value = "/donors/eligibility/{donorID}", method = RequestMethod.GET)
    boolean getEligibility(@PathVariable final Long donorID)
    {
        log.trace("getEligibility Entered!");

        Optional<Donor> donorOptional = donorService.findbyID(donorID);

        if(donorOptional.isPresent()==false)
            throw new RuntimeException("Attempted to get eligibility for an inexistent donor...");

        Donor donor = donorOptional.get();

        log.trace("getEligibility result: "+donor.getEligibility()+" \nexiting...");

        return donor.getEligibility();
    }

    @RequestMapping(value = "/bloodStocksUntested", method = RequestMethod.POST)
    public List<BloodDTO> collectBlood(@RequestBody Map<String, String> json)
    //@RequestBody final Long DonationID, @RequestBody final Float RQuantity, @RequestBody final Float PQuantity, @RequestBody final Float TQuantity, @RequestBody final String collectionDate)
    {
        return null;
    }

    @RequestMapping(value = "/bloodStocks/available", method = RequestMethod.GET)
    boolean checkAvailability(@RequestBody Map<String, String> json)
    //@RequestBody final Float Thrombocytes,@RequestBody final Float RedBloodCells,@RequestBody final Float Plasma)
    {
        return false;
    }

    @RequestMapping(value = "/donors/notify", method = RequestMethod.PUT)
    boolean notifyDonorNeeded(@RequestBody Map<String, String> json)
    //@RequestBody final String BloodType,@RequestBody final Boolean RH,@RequestBody final String Anticorps)
    {
        return false;
    }

    @RequestMapping(value = "/donors/compatibility", method = RequestMethod.GET)
    boolean checkCompatibility(@RequestBody Map<String, String> json)
    //@RequestBody final Long DonorID,@RequestBody final  Long PatientID)
    {
        return false;
    }

    @RequestMapping(value = "/requests/{requestID}", method = RequestMethod.PUT)
    boolean processRequest(@PathVariable final Long requestID)
    {
        //TODO : look for donors; if there exists a donor with same chars as person, set isdonor to true and act accordingly ;)

        return false;
    }
}
