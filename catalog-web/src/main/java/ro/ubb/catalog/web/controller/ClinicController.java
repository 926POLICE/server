package ro.ubb.catalog.web.controller;

import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ro.ubb.catalog.core.model.*;
import ro.ubb.catalog.core.repository.ClinicRepository;
import ro.ubb.catalog.core.repository.PersonnelRepository;
import ro.ubb.catalog.core.service.*;
import ro.ubb.catalog.core.utils.Utilities;
import ro.ubb.catalog.web.converter.*;
import ro.ubb.catalog.web.dto.*;

import javax.swing.text.html.Option;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by cata.
 */

@RestController
public class ClinicController {
    // workload handler

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
    private PersonnelService personnelService;

    @Autowired
    private ClinicRepository clinicRepository;

    @Autowired
    private PersonnelRepository personnelRepository;

    private BloodConverter bloodConverter = new BloodConverter();
    private DoctorConverter doctorConverter = new DoctorConverter();
    private DonationConverter donationConverter = new DonationConverter();
    private DonorConverter donorConverter = new DonorConverter();
    private PatientConverter patientConverter = new PatientConverter();
    private RequestConverter requestConverter = new RequestConverter();

    private Long currentTime = Instant.now().getEpochSecond();

    @RequestMapping(value = "/bloodStocks", method = RequestMethod.GET)
    public List<BloodDTO> getBloodStocks() {
        log.trace("getBloodStocks ENTERED!");

        List<Blood> bloodList = bloodService.getUsableBloods();

        log.trace("getBloodStocks EXITING : {}", bloodList);

        return bloodConverter.convertModelsToDtos(bloodList);
    }

    @RequestMapping(value = "/bloodStocksUntested", method = RequestMethod.GET)
    public List<BloodDTO> getUntestedBloodStocks()
    {
        log.trace("Get untested Bloods entered!");

        List<Blood> bloodList = bloodService.getUntestedBloods();

        log.trace("Get untested Bloods exited!");

        return bloodConverter.convertModelsToDtos(bloodList);
    }

    @RequestMapping(value = "/bloodStocksUnusable", method = RequestMethod.GET)
    public List<BloodDTO> getUnusableBloodStocks()
    {
        log.trace("Get Unusable Bloods entered&exited!");

        return bloodConverter.convertModelsToDtos(bloodService.getUnusableBloods());
    }

    @RequestMapping(value = "/patients", method = RequestMethod.GET)
    public List<PatientDTO> getPatients() {
        log.trace("getPatients ENTERED!");

        // lat/lng and math function to calculate the distance from a point x to a point y ;) (kudos to Prisacariu)
        List<Patient> patients = patientService.getAllPatients();
        List<Pair<Float, Patient>> pairs = new ArrayList<>();
        patients.forEach(p -> pairs.add(new Pair<>(Utilities.distance(p.getLatitude(), p.getLongitude(), clinicService.getTheClinic().getLatitude(), clinicService.getTheClinic().getLongitude()), p)));
        Collections.sort(pairs, (p1, p2) -> (p1.getKey().compareTo(p2.getKey())));
        List<Patient> result = new ArrayList<>();
        pairs.forEach(p -> result.add(p.getValue()));

        log.trace("getPatients EXITED! {}", result);

        List<PatientDTO> patientDTOList = new ArrayList<>();
        result.forEach(p -> patientDTOList.add(patientConverter.convertModelToDto(p)));
        return patientDTOList;
    }

    @RequestMapping(value = "/requests", method = RequestMethod.GET)
    public List<RequestDTO> getAllRequests() {
        List<Request> requests = requestService.getAllRequests();
        requests = requests.stream().filter(r -> !r.getCompleted()).collect(Collectors.toList());
        Collections.sort(requests);
        return requestConverter.convertModelsToDtos(requests);
    }

    @RequestMapping(value = "/pendingDonations/{donationid}", method = RequestMethod.DELETE)
    public ResponseEntity deletePendingDonation(@PathVariable final Long donationid) {
        log.trace("deletePendingDonation: donationid={}", donationid);

        donationService.deleteDonation(donationid);

        log.trace("deletePendingDonation - method end");

        return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.OK);
    }

    @RequestMapping(value = "/pendingDonations", method = RequestMethod.GET)
    public List<DonationDTO> getAllPendingDonations()
    {
        log.trace("GetAllPendingDonations entered!");

        List<Donation> donations = donationService.getAllDonations();

        donations = donations.stream().filter(d -> d.getR() == null).collect(Collectors.toList());

        log.trace("GetAllPendingDonations exited!");

        return donationConverter.convertModelsToDtos(donations);
    }

    @RequestMapping(value = "/bloodStocks/{bloodid}", method = RequestMethod.PUT)
    public BloodDTO updateBlood(@PathVariable final Long bloodid, @RequestBody Map<String, String> json) {
        log.trace("updateBlood: bloodId={}, bloodDtoMap={}", bloodid, json);

        Long collectiondate = Long.parseLong(json.get("collectiondate"));
        Float quantity = Float.parseFloat(json.get("quantity"));
        Integer state = Integer.parseInt(json.get("state"));
        String type = json.get("type");
        Boolean tested = Boolean.parseBoolean(json.get("tested"));
        Boolean usable = Boolean.parseBoolean(json.get("usable"));
        Long donationid = Long.parseLong(json.get("donationid"));
        Long clinicid = Long.parseLong(json.get("clinicid"));

        Optional<Blood> bloodOptional = bloodService.updateBlood(bloodid, collectiondate, quantity, state, type, tested, usable, donationid, clinicid);

        Map<String, BloodDTO> result = new HashMap<>();
        bloodOptional.ifPresent(blood -> result.put("blood", bloodConverter.convertModelToDto(blood)));

        log.trace("updateBlood: result={}", result);

        return result.get("blood");
    }

    @RequestMapping(value = "/bloodStocksUntested/{bloodid}", method = RequestMethod.POST)
    public BloodDTO testBlood(@PathVariable final Long bloodid, @RequestBody final boolean flag) {
        log.trace("testBlood: bloodId={}, flag={}", bloodid, flag);

        Optional<Blood> bloodOptional = bloodService.testBlood(bloodid, flag);

        Map<String, BloodDTO> result = new HashMap<>();
        bloodOptional.ifPresent(b -> result.put("blood", bloodConverter.convertModelToDto(b)));

        donorService.setLastAnalysisResult(bloodOptional.get().getDonation().getDonor().getId(),flag);

        log.trace("testBlood: result={}", result);

        return result.get("blood");
    }

    @RequestMapping(value = "/bloodStocksUnusable/{bloodid}", method = RequestMethod.DELETE)
    public ResponseEntity disposeBlood(@PathVariable final Long bloodid) {
        log.trace("disposeBlood: bloodId={}", bloodid);

        bloodService.deleteBlood(bloodid);

        log.trace("disposeBlood - method end");

        return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.OK);
    }

    @RequestMapping(value = "/bloodStocks/{bloodid}", method = RequestMethod.DELETE)
    public ResponseEntity useBlood(@PathVariable final Long bloodid) {
        log.trace("useBlood: bloodId={}", bloodid);

        bloodService.useBlood(bloodid);

        log.trace("useBlood - method end");

        return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.OK);
    }

    @RequestMapping(value = "/donors", method = RequestMethod.GET)
    public List<DonorDTO> getDonors() {
        log.trace("getDonors ENTERED!");

        List<Donor> donorList = donorService.getAllDonors();

        log.trace("getDonors EXITING : {}", donorList);

        return donorConverter.convertModelsToDtos(donorList);
    }

    @RequestMapping(value = "/donors/info/{donorid}", method = RequestMethod.PUT)
    public DonorDTO setDonorInfo(@PathVariable final Long donorid, @RequestBody Map<String, String> json) {
        log.trace("setDonorInfo entered");

        Optional<Donor> donorOptional = donorService.setInfo(donorid, json.get("bloodtype"), Boolean.parseBoolean(json.get("rh")), json.get("anticorps"));

        Map<String, DonorDTO> result = new HashMap<>();
        donorOptional.ifPresent(b -> result.put("donor", donorConverter.convertModelToDto(b)));

        log.trace("setDonorInfo exited");

        return result.get("donor");
    }

    @RequestMapping(value = "/donors/eligibility/{donorid}", method = RequestMethod.PUT)
    public DonorDTO setEligibility(@PathVariable final Long donorid, @RequestBody final boolean flag) {
        log.trace("setEligibility: bloodId={}, flag={}", donorid, flag);

        Optional<Donor> donorOptional = donorService.setEligibility(donorid, flag);

        Map<String, DonorDTO> result = new HashMap<>();
        donorOptional.ifPresent(b -> result.put("donor", donorConverter.convertModelToDto(b)));

        log.trace("setEligibility: result={}", result);

        return result.get("donor");
    }

    @RequestMapping(value = "/donors/eligibility/{donorid}", method = RequestMethod.GET)
    public boolean getEligibility(@PathVariable final Long donorid) {
        log.trace("getEligibility Entered!");

        Optional<Donor> donorOptional = donorService.findbyID(donorid);

        if (!donorOptional.isPresent())
            throw new RuntimeException("Attempted to get eligibility for an inexistent donor...");

        Donor donor = donorOptional.get();

        log.trace("getEligibility result: " + donor.getEligibility() + " \nexiting...");

        return donor.getEligibility() && donor.getNextDonation() <= currentTime;
    }

    @RequestMapping(value = "/donors/notified/{donorid}", method = RequestMethod.GET)
    public boolean getNotified(@PathVariable final Long donorid)
    {
        log.trace("getNotified Entered!");

        Optional<Donor> donorOptional = donorService.findbyID(donorid);

        if (!donorOptional.isPresent())
            throw new RuntimeException("Attempted to get notified for an inexistent donor...");

        Donor donor = donorOptional.get();

        log.trace("getNotified result: " + donor.getHasBeenNotified() + " \nexiting...");

        return donor.getHasBeenNotified();
    }

    @RequestMapping(value = "/bloodStocksUntested", method = RequestMethod.POST)
    public List<BloodDTO> collectBlood(@RequestBody Map<String, String> json) {
        log.trace("collectBlood entered!");

        /*
        // Will add blood to the donation with the associated ID a new blood container, placed automatically in the untested section.
@RequestMapping(value = "/bloodStocksUntested", method = RequestMethod.POST)
public List<BloodDTO> collectBlood(@RequestBody final Long DonationID, @RequestBody final Float RQuantity, @RequestBody final Float PQuantity, @RequestBody final Float TQuantity, @RequestBody final String collectionDate)
         */
        Long DonationID = Long.parseLong(json.get("donationid"));
        Float RQuantity = Float.parseFloat(json.get("rquantity"));
        Float PQuantity = Float.parseFloat(json.get("pquantity"));
        Float TQuantity = Float.parseFloat(json.get("tquantity"));
        Long collectionDate = Long.parseLong(json.get("collectiondate"));

        Optional<Donation> donationOptional = donationService.findByID(DonationID);
        if (!donationOptional.isPresent())
            throw new RuntimeException("Invalid donation!");

        // public Blood(Long collectionDate, Float quantity, Integer state, String type, Donation donation, Clinic clinic)
        Blood R = bloodService.createBlood(collectionDate, RQuantity, 1, "r", DonationID, clinicService.getTheClinic().getId());
        Blood P = bloodService.createBlood(collectionDate, PQuantity, 1, "p", DonationID, clinicService.getTheClinic().getId());
        Blood T = bloodService.createBlood(collectionDate, TQuantity, 1, "t", DonationID, clinicService.getTheClinic().getId());

        Donation donation = donationOptional.get();

        Donor donor = donation.getDonor();
        donorService.updateDonor(donor.getId(), donor.getName(), donor.getBirthday(), donor.getResidence(), donor.getAddress(), donor.getBloodType(), donor.getRh(), donor.getAnticorps(), donor.getIsDonor(), donor.getLatitude(), donor.getLongitude(), donor.getEligibility(), false, currentTime + 86400 * 56, donor.getUsername(), donor.getPassword());

        Long patientID=-1l;
        if(donation.getPatient()!=null)
            patientID = donation.getPatient().getId();
        donationService.updateDonation(donation.getId(), R.getId(), P.getId(), T.getId(), donation.getDonor().getId(), donation.getAnalysisResult(), patientID, donation.getClinic().getId());

        log.trace("collectBlood exited!");

        return Stream.of(bloodConverter.convertModelToDto(R), bloodConverter.convertModelToDto(P), bloodConverter.convertModelToDto(T)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/bloodStocks/available", method = RequestMethod.GET)
    public Float checkAvailability(@RequestBody Map<String, String> json) {
        /*
        // Returns true if the sum of the available stocks provides sufficient quantities for all the  specified components.
@RequestMapping(value = "/bloodStocks/available", method = RequestMethod.GET)
boolean checkAvailability(@RequestBody final Float Thrombocytes,@RequestBody final Float RedBloodCells,@RequestBody final Float Plasma);
         */

        Float R = Float.parseFloat(json.get("redbloodcells"));
        Float P = Float.parseFloat(json.get("plasma"));
        Float T = Float.parseFloat(json.get("thrombocytes"));

        return bloodService.checkAvailability(Request.builder().RQuantity(R).PQuantity(P).TQuantity(T).build());
    }

    @RequestMapping(value = "/donors/notify/{patientid}", method = RequestMethod.PUT)
    public boolean notifyDonorNeeded(@PathVariable final Long patientid) {
        /*
        // Will notify the closest donors that has passed the time since the last donation that match the given attributes.
// Calls for all available donors.
@RequestMapping(value = "/donors/notify", method = RequestMethod.PUT)
boolean notifyDonorNeeded(@RequestBody final String BloodType,@RequestBody final Boolean RH,@RequestBody final String Anticorps);
         */

        donorService.notifyDonorsNeeded(patientService.findByID(patientid).get());

        return false;
    }

    @RequestMapping(value = "/donors/compatibility", method = RequestMethod.GET)
    public boolean checkCompatibility(@RequestBody Map<String, String> json) {
        /*
        // Will check that the donor with ID is compatible with the patient with given ID. Return true ok and false if not. Throws an exception if either ID does not exist.
@RequestMapping(value = "/donors/compatibility", method = RequestMethod.GET)
boolean checkCompatibility(@RequestBody final Long DonorID,@RequestBody final  Long PatientID);
         */
        Long donorID = Long.parseLong(json.get("donorid"));
        Long patientID = Long.parseLong(json.get("patientid"));

        Optional<Donor> donorOptional = donorService.findbyID(donorID);
        Optional<Patient> patientOptional = patientService.findByID(patientID);

        if (!donorOptional.isPresent() || !patientOptional.isPresent())
            throw new RuntimeException("Invalid donor and/or patient!");

        Donor donor = donorOptional.get();
        Patient patient = patientOptional.get();

        return patient.isCompatible(donor);
    }

    @RequestMapping(value = "/requests/{requestid}", method = RequestMethod.PUT)
    public Float processRequest(@PathVariable final Long requestid) {
        Optional<Request> requestOptional = requestService.findByID(requestid);
        if (!requestOptional.isPresent())
            throw new RuntimeException("Invalid request ID!!");

        Request request = requestOptional.get();

        Float res = bloodService.checkAvailability(request);

        log.trace("ProcessRequest result: " + res.toString());

        if (res != 0)
            donorService.notifyDonorsNeeded(request.getPatient());
        else
        {
            requestService.updateRequest(request.getId(),request.getPatient().getId(),request.getDoctor().getId(),request.getRQuantity(),request.getPQuantity(),request.getTQuantity(),request.getPriority(),true,request.getClinic().getId());
            bloodService.honorRequest(request);
        }

        return res;
    }

    // Looks up for doctors/donors/personnel having the username and password. Returns a JSON with two fields: String Type (which can be 'doctor', 'donor', 'personnel' or 'invalid', if the username and password are invalid) and Long ID (the ID of the doctor/donor - will be -1 if invalid). The client program will remember the given ID and will use it during the session to send further requests.
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public LoginReply getUser(@RequestBody Map<String, String> json) {
        log.trace("getUser entered!");

        log.trace(json.get("username"));
        log.trace(json.get("password"));

        String username = json.get("username");
        String password = json.get("password");

        List<Personnel> personnelList = personnelService.getAllPersonnel();
        List<Doctor> doctorList = doctorService.getAllDoctors();
        List<Donor> donorList = donorService.getAllDonors();

        personnelList = personnelList.stream().filter(p -> p.getUsername().equals(username) && p.getPassword().equals(password)).collect(Collectors.toList());
        doctorList = doctorList.stream().filter(p -> p.getUsername().equals(username) && p.getPassword().equals(password)).collect(Collectors.toList());
        donorList = donorList.stream().filter(p -> p.getUsername().equals(username) && p.getPassword().equals(password)).collect(Collectors.toList());

        log.trace("getUser exited!");

        if (personnelList.size() == 0 && doctorList.size() == 0 && donorList.size() == 0)
            return new LoginReply("invalid", -1l);
        else if (doctorList.size() > 0)
            return new LoginReply("doctor", doctorList.get(0).getId());
        else if (donorList.size() > 0)
            return new LoginReply("donor", donorList.get(0).getId());
        else return new LoginReply("personnel", personnelList.get(0).getId());
    }
}
