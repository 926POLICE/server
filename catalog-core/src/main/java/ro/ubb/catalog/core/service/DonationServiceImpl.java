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

    @Override
    @Transactional
    public Optional<Donation> updateDonation(Long DonationID, Long RBloodID, Long PBloodID, Long TBloodID, Long donorID, Boolean analysisResult, Long patientID, Long donationClinicID) {
        log.trace("updateDonation Entered!");

        Optional<Donation> optionalDonation = donationRepository.findById(DonationID);

        Optional<Blood> RBlood = bloodRepository.findById(RBloodID);
        Optional<Blood> PBlood = bloodRepository.findById(PBloodID);
        Optional<Blood> TBlood = bloodRepository.findById(TBloodID);
        Optional<Donor> donor = donorRepository.findById(donorID);
        Optional<Patient> patientOptional = patientRepository.findById(patientID);
        if (!RBlood.isPresent() || !PBlood.isPresent() || !TBlood.isPresent() || !donor.isPresent())
            throw new RuntimeException("Invalid donation update!");

        Patient patient = null;
        if(patientOptional.isPresent())
            patient=patientOptional.get();

        Patient finalPatient = patient;
        optionalDonation.ifPresent(st -> {
            st.setR(RBlood.get());
            st.setP(PBlood.get());
            st.setT(TBlood.get());
            st.setDonor(donor.get());
            st.setPatient(finalPatient);
            st.setAnalysisResult(analysisResult);
        });

        log.trace("updateDonation Exited!");

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

    @Override
    @Transactional
    public void setResult(Long id, Boolean flag)
    {
        log.trace("SetDonationResult entered!");
        log.trace("Flag: "+flag.toString());
        Optional<Donation> optionalDonation = donationRepository.findById(id);

        optionalDonation.ifPresent(st -> {
            st.setAnalysisResult(flag);
        });

        log.trace("SetDonationResult exited!! Result:"+optionalDonation.get().toString());
    }


}
