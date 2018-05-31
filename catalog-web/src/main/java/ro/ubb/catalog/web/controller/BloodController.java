package ro.ubb.catalog.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ro.ubb.catalog.core.model.*;
import ro.ubb.catalog.core.repository.ClinicRepository;
import ro.ubb.catalog.core.repository.PersonnelRepository;
import ro.ubb.catalog.core.service.*;
import ro.ubb.catalog.web.converter.*;
import ro.ubb.catalog.web.dto.*;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by radu.
 */

@RestController
public class BloodController implements InitializingBean {

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

    @Autowired
    private ClinicController clinicController;

    @Autowired
    private DoctorController doctorController;

    @Autowired
    private DonorController donorController;

    private Long currentTime = Instant.now().getEpochSecond();

    private Boolean eraseAllFlag = true;

    private BloodConverter bloodConverter = new BloodConverter();
    private DoctorConverter doctorConverter = new DoctorConverter();
    private DonationConverter donationConverter = new DonationConverter();
    private DonorConverter donorConverter = new DonorConverter();
    private PatientConverter patientConverter = new PatientConverter();
    private RequestConverter requestConverter = new RequestConverter();

    private RestTemplate restTemplate = new RestTemplate();

    // http://www.baeldung.com/running-setup-logic-on-startup-in-spring
    @Override
    @Transactional
    public void afterPropertiesSet() throws Exception {
        if(eraseAllFlag==true)
        {
            clinicRepository.findAll().forEach(p->clinicRepository.deleteById(p.getId()));
            doctorService.getAllDoctors().forEach(d->doctorService.deleteDoctor(d.getId()));
            patientService.getAllPatients().forEach(p->patientService.deletePatient(p.getId()));
            donorService.getAllDonors().forEach(d->donorService.deleteDonor(d.getId()));
            donationService.getAllDonations().forEach(d->donationService.deleteDonation(d.getId()));
            requestService.getAllRequests().forEach(r->requestService.deleteRequest(r.getId()));
            bloodService.getAllBloods().forEach(b->bloodService.deleteBlood(b.getId()));
            personnelRepository.findAll().forEach(p->personnelRepository.deleteById(p.getId()));

            System.out.println("Cleared all the garbage sucessfully!");

            Clinic clinic = clinicRepository.save(new Clinic(46.67,23.50));
            Doctor doctor=doctorService.createDoctor("dre","dre","Dr. Dre","central");
            Patient patient=patientService.createPatient("ionut",1l,"a","b","A",false,"none",false,40.0,40.0,"central");
            Donor donor= donorService.createDonor("donor","donor","ionut",1l,"a","b","A",false,"none",false,1.0,2.0);
            Donation donation = donationService.createDonation(donor.getId(),null,clinicService.getTheClinic().getId());
            Request request = requestService.createRequest(patient.getId(),doctor.getId(),1.0f,2.0f,3.0f,1,clinic.getId());
            Blood blood = bloodService.createBlood(currentTime,2.0f,1,"r",donation.getId(),clinic.getId());
            bloodService.testBlood(blood.getId(),true);
            personnelRepository.save(new Personnel("admin","admin"));

            System.out.println(clinic);
            System.out.println(doctor);
            System.out.println(patient);
            System.out.println(donor);
            System.out.println(donation);
            System.out.println(request);
            System.out.println(blood);

            System.out.println("Added garbage sucessfully!");

            System.out.println(clinicRepository.findAll());
            System.out.println(doctorService.getAllDoctors());
            System.out.println(patientService.getAllPatients());
            System.out.println(donorService.getAllDonors());
            System.out.println(donationService.getAllDonations());
            System.out.println(bloodService.getAllBloods());
            System.out.println(requestService.getAllRequests());


            System.out.println("ALL OK");

            try
            {
                Map<String,String> theMap = new HashMap<>();

                Set<RequestDTO> requestDTOSet = doctorController.getAllDoctorRequests(doctor.getId());
                assert (requestDTOSet.size()==1);
//
                //@RequestMapping(value = "/requests", method = RequestMethod.POST)
                //RequestDTO newBloodRequest(@RequestBody final Long PatientID,@RequestBody final Long DoctorID, @RequestBody final Integer priority,@RequestBody final Float Rquantity, @RequestBody final Float Pquantity, @RequestBody final Float Tquantity)
                theMap.clear();
                theMap.put("patientid",patient.getId().toString());
                theMap.put("doctorid",doctor.getId().toString());
                theMap.put("priority",Integer.toString(1));
                theMap.put("rquantity",Double.toString(1.0));
                theMap.put("pquantity",Double.toString(1.0));
                theMap.put("tquantity",Double.toString(1.0));
                RequestDTO requestDTO = doctorController.newBloodRequest(theMap);
                requestDTOSet = doctorController.getAllDoctorRequests(doctor.getId());
                assert (requestDTOSet.size()==2);
                Request req = requestService.findByID(requestDTO.getId()).get();
                assert (req.getPriority()==1);

                /*
                // Returns a string with the status of the request.
@RequestMapping(value = "/requests/status", method = RequestMethod.GET)
String checkRequestStatus(@RequestBody final Long PatientID)
                 */
                String val = doctorController.checkRequestStatus(patient.getId());
                assert (val==Boolean.toString(false));

                // DOCTOR TESTED (still needs some 'minor' work)

                // DonorDTO registerUser(@RequestBody final String username,@RequestBody final String password,@RequestBody final String name,@RequestBody final String birthday,@RequestBody final String residence,@RequestBody final Double latitude,@RequestBody final Double longitude)
                theMap.clear();
                theMap.put("username","john");
                theMap.put("password","john");
                theMap.put("name","john");
                theMap.put("birthday",Long.toString(1l));
                theMap.put("residence","john");
                theMap.put("latitude",Double.toString(1.0f));
                theMap.put("longitude",Double.toString(1.0f));
                DonorDTO dd = donorController.registerUser(theMap);
                assert (donorService.getAllDonors().size()==2);
                assert (dd.getLatitude()==1.0f);

                try
                {
                    theMap.clear();
                    theMap.put("username","john");
                    theMap.put("password","john");
                    theMap.put("name","john");
                    theMap.put("birthday",Long.toString(1l));
                    theMap.put("residence","john");
                    theMap.put("latitude",Double.toString(1.0f));
                    theMap.put("longitude",Double.toString(1.0f));
                    donorController.registerUser(theMap);
                    throw new RuntimeException("YEE!");
                }
                catch (Exception e)
                {
                    // nothing to do here
                }

                //    @RequestMapping(value = "/donors/history/{donorID}", method = RequestMethod.GET)
                //    List<DonationDTO> getAnalysisHistory(@PathVariable final Long donorID)

                List<DonationDTO> donationDTOList = donorController.getAnalysisHistory(donor.getId());
                assert (donationDTOList.size()==1);
                assert (donationDTOList.get(0).getClinicid()==clinicService.getTheClinic().getId());

                //    @RequestMapping(value = "/donors/nextDonation/{donorID}", method = RequestMethod.GET)
                //    String getNextDonation(@PathVariable final Long donorID)

                donorService.updateDonor(donor.getId(),donor.getName(),donor.getBirthday(),doctor.getHospital(),donor.getAddress(),donor.getBloodType(),donor.getRh(),donor.getAnticorps(),donor.getIsDonor(),donor.getLatitude(),donor.getLongitude(),donor.getEligibility(),blood.getCollectionDate() + 86400 * 56,donor.getUsername(),donor.getPassword());

                String nextDonation = donorController.getNextDonation(donor.getId());

                log.trace("Next donation is:");
                log.trace(nextDonation);
                Long nextDonationLong = Long.parseLong(nextDonation);

                assert (nextDonationLong == blood.getCollectionDate() + 86400 * 56);

                //     @RequestMapping(value = "/donors/bloodContainers/{donorID}", method = RequestMethod.GET)
                //    List<BloodDTO> getBloodJourney(@PathVariable final Long donorID)
                List<BloodDTO> bloodDTOList = donorController.getBloodJourney(donor.getId());
                assert (bloodDTOList.size()==1);
                assert (bloodDTOList.get(0).getQuantity()==blood.getQuantity());
                assert (bloodDTOList.get(0).getState()==1);

                //     @RequestMapping(value = "/donors/{donorID}", method = RequestMethod.PUT)
                //DonorDTO updatePersonalDetails(@PathVariable final  Long DonorID, @RequestBody final String name,@RequestBody final String birthday,@RequestBody final String residence,@RequestBody final Double latitude,@RequestBody final Double longitude)
                theMap.clear();
                theMap.put("name","ares");
                theMap.put("birthday",Long.toString(donor.getBirthday()));
                theMap.put("residence","homestead");
                theMap.put("latitude",Double.toString(1.0f));
                theMap.put("longitude",Double.toString(1.0f));
                donorController.updatePersonalDetails(donor.getId(),theMap);

                Donor testDonor = donorService.findbyID(donor.getId()).get();

                assert testDonor.getName().equals("ares");

                // DonorDTO getPersonalDetails(@PathVariable final  Long donorID)
                DonorDTO donorDTO = donorController.getPersonalDetails(donor.getId());
                assert (donor.getName()==donorDTO.getName());
                assert (donor.getResidence()==donorDTO.getResidence());

                // String updateMedicalHistory(@PathVariable final Long donorID, @RequestBody final String newHistory)
                String history = donor.getMedicalHistory();
                donorController.updateMedicalHistory(donor.getId(),"blabla");
                testDonor = donorService.findbyID(donor.getId()).get();
                assert (testDonor.getMedicalHistory()==(history+"blabla"));

                // DonationDTO donate(@RequestBody Map<String, String> json)
                // @RequestBody final Long DonorID, @RequestBody final @Nullable Long PatientID

                // Set<DonationDTO> getAllPendingDonations()
                assert (clinicController.getAllPendingDonations().size()==0);

                theMap.clear();
                theMap.put("donorid",Long.toString(donor.getId()));
                theMap.put("patientid",Long.toString(patient.getId()));
                DonationDTO res = donorController.donate(theMap);
                assert (donationService.getAllDonations().size()==2);
                assert (res.getDonorid()==donor.getId());
                assert (res.getAnalysisresult()==false);

                // --- Donor Controller tested

                //     @RequestMapping(value = "/bloodStocks", method = RequestMethod.GET)
                //    Set<BloodDTO> getBloodStocks()
                Blood newBlood = bloodService.createBlood(1l,20f,2,"p",donation.getId(),clinic.getId());
                Set<BloodDTO> bloodDTOSet = clinicController.getBloodStocks();
                bloodDTOList = bloodDTOSet.stream().collect(Collectors.toList());
                assert (bloodDTOList.get(0).getUsable()==true);
                assert (bloodDTOList.get(0).getId()==blood.getId());
                assert (bloodDTOSet.size()==1);
                assert (bloodService.getAllBloods().size()==2);

                // public List<Blood> getUnusableBloods()
                clinicController.testBlood(blood.getId(),false);
                bloodDTOSet = clinicController.getUnusableBloodStocks();
                assert (bloodDTOSet.size()==1);
                bloodDTOList = bloodDTOSet.stream().collect(Collectors.toList());
                assert (bloodDTOList.get(0).getId()==blood.getId());

                // public List<Blood> getUntestedBloods()
                bloodDTOSet = clinicController.getUntestedBloodStocks();
                assert (bloodDTOSet.size()==1);
                bloodDTOList = bloodDTOSet.stream().collect(Collectors.toList());
                assert (bloodDTOList.get(0).getQuantity()==20f);

                // testBlood
                clinicController.testBlood(newBlood.getId(),true);
                bloodDTOSet = clinicController.getBloodStocks();
                assert (bloodDTOSet.size()==2);
                List<Long> bloodDTOIDs = bloodDTOSet.stream().map(b->b.getId()).collect(Collectors.toList());
                assert (bloodDTOIDs.contains(newBlood.getId()));

                //Set<PatientDTO> getPatients()
                patientService.createPatient("gigel",2l,"a","b","AB",false,"unknown",false,1.0d,2.0d,"east");
                List<PatientDTO> patientDTOList = clinicController.getPatients();
                assert (patientDTOList.size()==2);
                assert (patientDTOList.get(0).getLatitude()==40.0);

                // Set<RequestDTO> getAllRequests()
                requestDTOSet = clinicController.getAllRequests();
                assert (requestDTOSet.size()==1);
                requestService.updateRequest(request.getId(),request.getPatient().getId(),req.getDoctor().getId(),1.0f,2.0f,3.0f,2,true,clinic.getId());
                requestDTOSet = clinicController.getAllRequests();
                assert (requestDTOSet.size()==0);

                // Set<DonationDTO> getAllPendingDonations()
                Set<DonationDTO> donationDTOSet = clinicController.getAllPendingDonations();
                assert (donationDTOSet.size()==1);

                // Set<DonorDTO> getDonors()
                Set<DonorDTO> donorDTOSet = clinicController.getDonors();
                assert (donorDTOSet.size()==1);
                List<DonorDTO> donorDTOList = donorDTOSet.stream().collect(Collectors.toList());
                assert (donorDTOList.get(0).getId()==donor.getId());

                /*
                	// Will set the given parameters to the donor with the given ID.
	// Parameters: bloodtype - string, rh - boolean, anticorps - string.
    @RequestMapping(value = "/donors/info/{donorID}", method = RequestMethod.PUT)
    DonorDTO setDonorInfo(@PathVariable final Long donorID, @RequestBody Map<String, String> json);
                 */
                theMap.clear();
                theMap.put("bloodtype","0");
                theMap.put("rh",Boolean.toString(true));
                theMap.put("anticorps","are");
                testDonor = donorService.findbyID(donor.getId()).get();
                assert (!donor.getAnticorps().equals("are"));
                clinicController.setDonorInfo(donor.getId(),theMap);
                assert (donor.getAnticorps().equals("are"));

                assert (clinicController.getEligibility(donor.getId())==false);
                clinicController.setEligibility(donor.getId(),true);
                assert (clinicController.getEligibility(donor.getId())==true);

                // LoginReply getUser(@RequestBody Map<String, String> json)
                theMap.clear();
                theMap.put("username","gibberish");
                theMap.put("password","gibberish");
                LoginReply reply = clinicController.getUser(theMap);
                assert (reply.getId()==-1l);
                assert (reply.getType().equals("invalid"));

                theMap.clear();
                theMap.put("username",donor.getUsername());
                theMap.put("password",donor.getPassword());
                reply = clinicController.getUser(theMap);
                assert (reply.getId()==-donor.getId());
                assert (reply.getType().equals("donor"));

                theMap.clear();
                theMap.put("username",doctor.getUsername());
                theMap.put("password",doctor.getPassword());
                reply = clinicController.getUser(theMap);
                assert (reply.getId()==-doctor.getId());
                assert (reply.getType().equals("doctor"));

                theMap.clear();
                theMap.put("username","admin");
                theMap.put("password","admin");
                reply = clinicController.getUser(theMap);
                assert (reply.getType().equals("personnel"));

                // checkAvailability redbloodcells plasma thrombocytes
                theMap.clear();
                theMap.put("redbloodcells",Float.toString(2.0f));
                theMap.put("plasma",Float.toString(0.0f));
                theMap.put("thrombocytes",Float.toString(0.0f));
                assert (clinicController.checkAvailability(theMap)==0.0f);
                theMap.put("plasma",Float.toString(1.0f));
                assert (clinicController.checkAvailability(theMap)==1.0f);

                // boolean checkCompatibility(@RequestBody Map<String, String> json)
                theMap.clear();
                theMap.put("donorid",Long.toString(donor.getId()));
                theMap.put("patientid",Long.toString(patient.getId()));
                assert (clinicController.checkCompatibility(theMap)==false);
                patientService.updatePatient(patient.getId(),patient.getName(),patient.getBirthday(),patient.getResidence(),patient.getAddress(),donor.getBloodType(),donor.getRh(),donor.getAnticorps(), patient.getIsDonor(),patient.getLatitude(),patient.getLongitude(),patient.getHospital());
                assert (clinicController.checkCompatibility(theMap)==true);

                //@RequestMapping(value = "/bloodStocks/{bloodId}", method = RequestMethod.PUT)
                //BloodDTO updateBlood(@PathVariable final Long bloodId, @RequestBody final BloodDTO bloodDto)
                /*
                    private Long collectiondate;
    private Float quantity;
    private Integer state;
    private String type;
    private Integer shelflife;
    private Boolean tested;
    private Boolean usable;
    private Long donationid;
    private Long clinicid;
                 */

                // HERE !

                // keep these at the end!

                // dispose blood
                clinicController.disposeBlood(newBlood.getId());
                assert (clinicController.getUnusableBloodStocks().size()==0);
                // use blood
                clinicController.useBlood(blood.getId());
                assert (clinicController.getBloodStocks().size()==0);

                log.trace("ALL TESTS PASSED!");
            }
            catch (Exception e)
            {
                log.trace("TESTING FAILED: ");
                log.trace(e.getMessage()+ " " + e.getLocalizedMessage());
            }
        }
        else
        {
            System.out.println(clinicRepository.findAll());
            System.out.println(doctorService.getAllDoctors());
            System.out.println(patientService.getAllPatients());
            System.out.println(donorService.getAllDonors());
            System.out.println(donationService.getAllDonations());
            System.out.println(bloodService.getAllBloods());
            System.out.println(requestService.getAllRequests());

            System.out.println("ALL OK");
        }
    }

        @RequestMapping(value = "/bloods", method = RequestMethod.GET)
    public Set<BloodDTO> getBloodsList() {
        log.trace("getBloods");

        try
        {
            // Used for debugging.
            log.trace("BEGIN DEBUG");
            log.trace(clinicService.getTheClinic().getBloodStock().toString());
            log.trace("END DEBUG");
        }
        catch (Exception e)
        {
            log.trace("DEBUG FAIL: "+e.getMessage());
        }

        List<Blood> bloods = bloodService.getAllBloods();

        log.trace("getBloods: bloods={}", bloods);

        BloodConverter c = new BloodConverter();
        return c.convertModelsToDtos(bloods);
    }

    @SuppressWarnings("Duplicates")
    @RequestMapping(value = "/bloods/{bloodId}", method = RequestMethod.PUT)
    public BloodDTO updateBlood(@PathVariable final Long bloodId, @RequestBody final BloodDTO bloodDto)
    {
        log.trace("updateBlood: bloodId={}, bloodDtoMap={}", bloodId, bloodDto);

        Optional<Blood> bloodOptional = bloodService.updateBlood(bloodId,bloodDto.getCollectiondate(),bloodDto.getQuantity(),bloodDto.getState(),bloodDto.getType(),bloodDto.getTested(),bloodDto.getUsable(),bloodDto.getDonationid(),bloodDto.getClinicid());

        Map<String, BloodDTO> result = new HashMap<>();
        bloodOptional.ifPresent(blood -> result.put("blood", bloodConverter.convertModelToDto(blood)));

        log.trace("updateBlood: result={}", result);

        return result.get("blood");
    }


    @RequestMapping(value = "/bloods", method = RequestMethod.POST)
    public BloodDTO createBlood(@RequestBody final BloodDTO bloodDto) {
        log.trace("createBlood: bloodDtoMap={}", bloodDto);

        Blood blood = null;

        try {
            blood = bloodService.createBlood(bloodDto.getCollectiondate(), bloodDto.getQuantity(), bloodDto.getState(), bloodDto.getType(), bloodDto.getDonationid(), bloodDto.getClinicid());
        }
        catch (Exception e)
        {
            log.trace(e.getMessage());
        }

        BloodDTO result = bloodConverter.convertModelToDto(blood);

        log.trace("createBlood: result={}", result);
        return result;
    }


    @RequestMapping(value = "/bloods/{bloodId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteBlood(@PathVariable final Long bloodId) {
        log.trace("deleteBlood: bloodId={}", bloodId);

        bloodService.deleteBlood(bloodId);

        log.trace("deleteBlood - method end");

        return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.OK);
    }
}
