package ro.ubb.catalog.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.ubb.catalog.core.model.*;
import ro.ubb.catalog.core.repository.*;

import java.util.List;
import java.util.Optional;

@Service
public class DonationServiceImpl implements DonationService {
    private static final Logger log = LoggerFactory.getLogger(DonationServiceImpl.class);

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private BloodRepository bloodRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DonorRepository donorRepository;

    @Autowired
    private ClinicService clinicService;

    @Override
    public List<Donation> getAllDonations() {
        log.trace("getAllDonations --- method entered");

        List<Donation> donations = donationRepository.findAll();

        log.trace("getAllDonations: donations={}", donations);

        return donations;
    }

    @Override
    public Donation createDonation(Long donorID, Long patientID, Long donationClinicID) {
        if (patientID == null)
            return donationRepository.save(new Donation(null, null, null, false, donorRepository.findById(donorID).get(), null, clinicService.getTheClinic()));
        else
            return donationRepository.save(new Donation(null, null, null, false, donorRepository.findById(donorID).get(), patientRepository.findById(patientID).get(), clinicService.getTheClinic()));
    }

//    @Override
//    public Donation createDonation(Long RBloodID, Long PBloodID, Long TBloodID, Long donorID, Long patientID, Long donationClinicID) {
//        Optional<Blood> RBlood = bloodRepository.findById(RBloodID);
//        Optional<Blood> PBlood = bloodRepository.findById(PBloodID);
//        Optional<Blood> TBlood = bloodRepository.findById(TBloodID);
//        Optional<Donor> donor = donorRepository.findById(donorID);
//        Optional<Patient> patient = patientRepository.findById(patientID);
//        Optional<Clinic> clinic = clinicRepository.findById(donationClinicID);
//
//        Blood R=null, P=null, T=null;
//        Donor d=null;
//        Patient p=null;
//        if(RBlood.isPresent()==true)
//            R=RBlood.get();
//        if(PBlood.isPresent()==false)
//            P = null;
//        if(TBlood.isPresent()==false)
//            T = null;
//
//        Donation donation = donationRepository.save(new Donation(RBlood.get(),PBlood.get(),TBlood.get(),false,donor.get(),patient.get(),clinic.get()));
//        return donation;
//    }

    @Override
    @Transactional
    public Optional<Donation> updateDonation(Long DonationID, Long RBloodID, Long PBloodID, Long TBloodID, Long donorID, Boolean analysisResult, Long patientID, Long donationClinicID) {
        Optional<Donation> optionalDonation = donationRepository.findById(DonationID);

        Optional<Blood> RBlood = bloodRepository.findById(RBloodID);
        Optional<Blood> PBlood = bloodRepository.findById(PBloodID);
        Optional<Blood> TBlood = bloodRepository.findById(TBloodID);
        Optional<Donor> donor = donorRepository.findById(donorID);
        Optional<Patient> patient = patientRepository.findById(patientID);
        if (RBlood.isPresent() == false || PBlood.isPresent() == false || TBlood.isPresent() == false || donor.isPresent() == false || patient.isPresent() == false)
            throw new RuntimeException("Invalid donation update!");

        optionalDonation.ifPresent(st -> {
            st.setR(RBlood.get());
            st.setP(PBlood.get());
            st.setT(TBlood.get());
            st.setDonor(donor.get());
            st.setPatient(patient.get());
            st.setAnalysisResult(analysisResult);
        });

        return optionalDonation;
    }

    @Override
    public void deleteDonation(Long id) {
        log.trace("deleteDonation: id={}", id);

        donationRepository.deleteById(id);

        log.trace("deleteDonation --- method finished");
    }

    @Override
    public Optional<Donation> findByID(Long id) {
        return donationRepository.findById(id);
    }


}
