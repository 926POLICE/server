package ro.ubb.catalog.web.controller;

import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.ubb.catalog.core.model.*;
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
public class ClinicController
{
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

    private BloodConverter bloodConverter = new BloodConverter();
    private DoctorConverter doctorConverter = new DoctorConverter();
    private DonationConverter donationConverter = new DonationConverter();
    private DonorConverter donorConverter = new DonorConverter();
    private PatientConverter patientConverter = new PatientConverter();
    private RequestConverter requestConverter = new RequestConverter();

    private Long currentTime = Instant.now().getEpochSecond();

    @RequestMapping(value = "/bloodStocks", method = RequestMethod.GET)
    Set<BloodDTO> getBloodStocks()
    {
        log.trace("getBloodStocks ENTERED!");

        List<Blood> bloodList = bloodService.getAllBloods();
        bloodList = bloodList.stream().filter(b->b.getTested()==true && b.getCollectionDate()+86400*b.getShelfLife()>= currentTime && b.getState()!=3).collect(Collectors.toList());

        log.trace("getBloodStocks EXITING : {}",bloodList);

        return bloodConverter.convertModelsToDtos(bloodList);
    }

    @RequestMapping(value = "/bloodStocksUntested", method = RequestMethod.GET)
    Set<BloodDTO> getUntestedBloodStocks()
    {
        List<Blood> bloodList = bloodService.getAllBloods();
        bloodList = bloodList.stream().filter(p->p.getTested()==false).collect(Collectors.toList());
        return bloodConverter.convertModelsToDtos(bloodList);
    }

    @RequestMapping(value = "/bloodStocksUnusable", method = RequestMethod.GET)
    Set<BloodDTO> getUnusableBloodStocks()
    {
        Set<Blood> to_return = new HashSet<>();
        List<Blood> bloodList = bloodService.getAllBloods();
        List<Blood> unusableBlood = bloodList.stream().filter(b->b.getUsable()==false).collect(Collectors.toList());
        List<Blood> expiredBlood = bloodList.stream().filter(b->b.getCollectionDate()+86400*b.getShelfLife()< currentTime).collect(Collectors.toList());
        to_return.addAll(unusableBlood);
        to_return.addAll(expiredBlood);
        return bloodConverter.convertModelsToDtos(to_return);
    }

    @RequestMapping(value = "/patients", method = RequestMethod.GET)
    Set<PatientDTO> getPatients()
    {
        log.trace("getPatients ENTERED!");

        // lat/lng and math function to calculate the distance from a point x to a point y ;) (kudos to Prisacariu)
        List<Patient> patients = patientService.getAllPatients();
        List<Pair<Double,Patient>> pairs = new ArrayList<>();
        patients.forEach(p->pairs.add(new Pair<>(Utilities.distance(p.getLatitude(),p.getLongitude(),clinicService.getTheClinic().getLatitude(),clinicService.getTheClinic().getLongitude()),p)));
        Collections.sort(pairs,(p1,p2)->(p1.getKey().compareTo(p2.getKey())));
        List<Patient> result = new ArrayList<>();
        pairs.forEach(p->result.add(p.getValue()));

        log.trace("getPatients EXITED! {}",result);

        return patientConverter.convertModelsToDtos(result);
    }

    @RequestMapping(value = "/requests", method = RequestMethod.GET)
    Set<RequestDTO> getAllRequests()
    {
        List<Request> requests = requestService.getAllRequests();
        requests = requests.stream().filter(r->r.getCompleted()==false).collect(Collectors.toList());
        Collections.sort(requests);
        return requestConverter.convertModelsToDtos(requests);
    }

    @RequestMapping(value = "/pendingDonations", method = RequestMethod.GET)
    Set<DonationDTO> getAllPendingDonations()
    {
        List<Donation> donations = donationService.getAllDonations();
        donations = donations.stream().filter(d->d.getR()==null).collect(Collectors.toList());
        return donationConverter.convertModelsToDtos(donations);
    }

    @RequestMapping(value = "/bloodStocks/{bloodId}", method = RequestMethod.PUT)
    BloodDTO updateBlood(@PathVariable final Long bloodId, @RequestBody final BloodDTO bloodDto)
    {
        log.trace("updateBlood: bloodId={}, bloodDtoMap={}", bloodId, bloodDto);

        Optional<Blood> bloodOptional = bloodService.updateBlood(bloodId,bloodDto.getCollectionDate(),bloodDto.getQuantity(),bloodDto.getState(),bloodDto.getType(),bloodDto.getTested(),bloodDto.getUsable(),bloodDto.getDonationID(),bloodDto.getClinicID());

        Map<String, BloodDTO> result = new HashMap<>();
        bloodOptional.ifPresent(blood -> result.put("blood", bloodConverter.convertModelToDto(blood)));

        log.trace("updateBlood: result={}", result);

        return result.get("blood");
    }

    @RequestMapping(value = "/bloodStocksUntested/{bloodId}", method = RequestMethod.PUT)
    BloodDTO testBlood(@PathVariable final Long bloodId, @RequestBody final boolean flag)
    {
        log.trace("setBloodFlag: bloodId={}, flag={}", bloodId, flag);

        Optional<Blood> bloodOptional = bloodService.testBlood(bloodId,flag);

        Map<String, BloodDTO> result = new HashMap<>();
        bloodOptional.ifPresent(b -> result.put("blood", bloodConverter.convertModelToDto(b)));

        bloodOptional.get().getDonation().setAnalysisResult(flag);
        bloodOptional.get().getDonation().getDonor().setLastAnalysisResult(flag);

        log.trace("setBloodFlag: result={}", result);

        return result.get("blood");
    }

    @RequestMapping(value = "/bloodStocksUnusable/{bloodId}", method = RequestMethod.DELETE)
    public ResponseEntity disposeBlood(@PathVariable final Long bloodId)
    {
        log.trace("disposeBlood: bloodId={}", bloodId);

        bloodService.useBlood(bloodId);

        log.trace("disposeBlood - method end");

        return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.OK);
    }

    @RequestMapping(value = "/bloodStocks/{bloodId}", method = RequestMethod.DELETE)
    public ResponseEntity useBlood(@PathVariable final Long bloodId)
    {
        log.trace("useBlood: bloodId={}", bloodId);

        bloodService.deleteBlood(bloodId);

        log.trace("useBlood - method end");

        return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.OK);
    }

    @RequestMapping(value = "/donors", method = RequestMethod.GET)
    Set<DonorDTO> getDonors()
    {
        log.trace("getDonors ENTERED!");

        List<Donor> donorList = donorService.getAllDonors();

        log.trace("getDonors EXITING : {}",donorList);

        return donorConverter.convertModelsToDtos(donorList);
    }

    @RequestMapping(value = "/donors/info/{donorID}", method = RequestMethod.PUT)
    DonorDTO setDonorInfo(@PathVariable final Long donorID, @RequestBody Map<String, String> json)
    {
        log.trace("setDonorInfo entered");

        Optional<Donor> donorOptional = donorService.setInfo(donorID,json.get("bloodtype"),Boolean.parseBoolean(json.get("rh")),json.get("anticorps"));

        Map<String, DonorDTO> result = new HashMap<>();
        donorOptional.ifPresent(b -> result.put("donor", donorConverter.convertModelToDto(b)));

        log.trace("setDonorInfo exited");

        return result.get("donor");
    }

    @RequestMapping(value = "/donors/eligibility/{donorID}", method = RequestMethod.PUT)
    DonorDTO setEligibility(@PathVariable final Long donorID, @RequestBody final boolean flag)
    {
        log.trace("setEligibility: bloodId={}, flag={}", donorID, flag);

        Optional<Donor> donorOptional = donorService.setEligibility(donorID,flag);

        Map<String, DonorDTO> result = new HashMap<>();
        donorOptional.ifPresent(b -> result.put("donor", donorConverter.convertModelToDto(b)));

        log.trace("setEligibility: result={}", result);

        return result.get("donor");
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

        return donor.getEligibility() && donor.getNextDonation() <= currentTime ;
    }

    @RequestMapping(value = "/bloodStocksUntested", method = RequestMethod.POST)
    public Set<BloodDTO> collectBlood(@RequestBody Map<String, String> json)
    {
        /*
        // Will add blood to the donation with the associated ID a new blood container, placed automatically in the untested section.
@RequestMapping(value = "/bloodStocksUntested", method = RequestMethod.POST)
public List<BloodDTO> collectBlood(@RequestBody final Long DonationID, @RequestBody final Float RQuantity, @RequestBody final Float PQuantity, @RequestBody final Float TQuantity, @RequestBody final String collectionDate)
         */
        Long DonationID = Long.parseLong(json.get("DonationID"));
        Float RQuantity = Float.parseFloat(json.get("RQuantity"));
        Float PQuantity = Float.parseFloat(json.get("PQuantity"));
        Float TQuantity = Float.parseFloat(json.get("TQuantity"));
        Long collectionDate = Long.parseLong(json.get("collectionDate"));

        Optional<Donation> donationOptional = donationService.findByID(DonationID);
        if(donationOptional.isPresent()==false)
            throw new RuntimeException("Invalid donation!");

        // public Blood(Long collectionDate, Float quantity, Integer state, String type, Donation donation, Clinic clinic)
        Blood R = bloodService.createBlood(collectionDate,RQuantity,1,"r",DonationID,clinicService.getTheClinic().getId());
        Blood P = bloodService.createBlood(collectionDate,PQuantity,1,"p",DonationID,clinicService.getTheClinic().getId());
        Blood T = bloodService.createBlood(collectionDate,TQuantity,1,"t",DonationID,clinicService.getTheClinic().getId());

        Donation donation = donationOptional.get();
        Donor donor = donation.getDonor();
        donor.setNextDonation(currentTime+86400 * 56);
        donor.setHasBeenNotified(false);

        return Stream.of(bloodConverter.convertModelToDto(R),bloodConverter.convertModelToDto(P),bloodConverter.convertModelToDto(T)).collect(Collectors.toSet());
    }

    @RequestMapping(value = "/bloodStocks/available", method = RequestMethod.GET)
    Float checkAvailability(@RequestBody Map<String, String> json)
    {
        /*
        // Returns true if the sum of the available stocks provides sufficient quantities for all the  specified components.
@RequestMapping(value = "/bloodStocks/available", method = RequestMethod.GET)
boolean checkAvailability(@RequestBody final Float Thrombocytes,@RequestBody final Float RedBloodCells,@RequestBody final Float Plasma);
         */

        Float R = Float.parseFloat(json.get("RedBloodCells"));
        Float P = Float.parseFloat(json.get("Plasma"));
        Float T = Float.parseFloat(json.get("Thrombocytes"));

        return bloodService.checkAvailability(R,P,T);
    }

    @RequestMapping(value = "/donors/notify", method = RequestMethod.PUT)
    boolean notifyDonorNeeded(@RequestBody Map<String, String> json)
    {
        /*
        // Will notify the closest donors that has passed the time since the last donation that match the given attributes.
// Calls for all available donors.
@RequestMapping(value = "/donors/notify", method = RequestMethod.PUT)
boolean notifyDonorNeeded(@RequestBody final String BloodType,@RequestBody final Boolean RH,@RequestBody final String Anticorps);
         */
        String bloodType = json.get("BloodType");
        Boolean Rh = Boolean.parseBoolean(json.get("RH"));
        String anticorps = json.get("Anticorps");

        donorService.notifyDonorsNeeded(bloodType,Rh,anticorps);

        return false;
    }

    @RequestMapping(value = "/donors/compatibility", method = RequestMethod.GET)
    boolean checkCompatibility(@RequestBody Map<String, String> json)
    {
        /*
        // Will check that the donor with ID is compatible with the patient with given ID. Return true ok and false if not. Throws an exception if either ID does not exist.
@RequestMapping(value = "/donors/compatibility", method = RequestMethod.GET)
boolean checkCompatibility(@RequestBody final Long DonorID,@RequestBody final  Long PatientID);
         */
        Long donorID = Long.parseLong(json.get("DonorID"));
        Long patientID = Long.parseLong(json.get("PatientID"));

        Optional<Donor> donorOptional = donorService.findbyID(donorID);
        Optional<Patient> patientOptional = patientService.findByID(patientID);

        if(donorOptional.isPresent()==false || patientOptional.isPresent()==false)
            throw new RuntimeException("Invalid donor and/or patient!");

        Donor donor = donorOptional.get();
        Patient patient = patientOptional.get();

        return patient.isCompatible(donor);
    }

    @RequestMapping(value = "/requests/{requestID}", method = RequestMethod.PUT)
    Float processRequest(@PathVariable final Long requestID)
    {
        Optional<Request> requestOptional = requestService.findByID(requestID);
        if(requestOptional.isPresent()==false)
            throw new RuntimeException("Invalid request ID!!");

        Request request = requestOptional.get();

        Float res = bloodService.checkAvailability(request.getRQuantity(),request.getPQuantity(),request.getTQuantity());

        if(res!=0)
            donorService.notifyDonorsNeeded(request.getPatient().getBloodType(),request.getPatient().getRh(),request.getPatient().getAnticorps());

        return res;
    }

    // Looks up for doctors/donors/personnel having the username and password. Returns a JSON with two fields: String Type (which can be 'doctor', 'donor', 'personnel' or 'invalid', if the username and password are invalid) and Long ID (the ID of the doctor/donor - will be -1 if invalid). The client program will remember the given ID and will use it during the session to send further requests.
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    LoginReply getUser(@RequestBody Map<String, String> json)
    {
        String username = json.get("username");
        String password = json.get("password");

        List<Personnel> personnelList = personnelService.getAllPersonnel();
        List<Doctor> doctorList = doctorService.getAllDoctors();
        List<Donor> donorList = donorService.getAllDonors();

        personnelList = personnelList.stream().filter(p->p.getUsername()==username&&p.getPassword()==password).collect(Collectors.toList());
        doctorList = doctorList.stream().filter(p->p.getUsername()==username&&p.getPassword()==password).collect(Collectors.toList());
        donorList = donorList.stream().filter(p->p.getUsername()==username&&p.getPassword()==password).collect(Collectors.toList());

        if(personnelList.size()==0 && doctorList.size()==0 && donorList.size()==0)
            return new LoginReply("invalid",-1l);
        else if (doctorList.size()>0)
            return new LoginReply("doctor",doctorList.get(0).getId());
        else if (donorList.size()>0)
            return new LoginReply("donor",donorList.get(0).getId());
        else return new LoginReply("personnel",personnelList.get(0).getId());
    }
}
