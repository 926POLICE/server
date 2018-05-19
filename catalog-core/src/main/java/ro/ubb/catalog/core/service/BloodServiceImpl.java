package ro.ubb.catalog.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.ubb.catalog.core.model.Blood;
import ro.ubb.catalog.core.model.Donation;
import ro.ubb.catalog.core.model.Clinic;
import ro.ubb.catalog.core.repository.BloodRepository;
import ro.ubb.catalog.core.repository.ClinicRepository;
import ro.ubb.catalog.core.repository.DonationRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BloodServiceImpl implements BloodService
{
    private static final Logger log = LoggerFactory.getLogger(BloodServiceImpl.class);

    @Autowired
    private BloodRepository bloodRepository;

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private ClinicRepository clinicRepository;

    @Override
    public List<Blood> getAllBloods() {
        log.trace("getAllBloods --- method entered");

        List<Blood> bloods = bloodRepository.findAll();

        log.trace("getAllBloods: bloods={}", bloods);

        return bloods;
    }

    @Override
    public Blood createBlood(Long collectionDate, Float quantity, Integer state, String type, Long DonationID, Long ClinicID)
    {
        log.trace("createBlood - method Entered");

        Optional<Donation> donationOptional = donationRepository.findById(DonationID);
        Donation donation = null;

        if(donationOptional.isPresent())
            donation = donationOptional.get();
        else
        {
            log.trace("createBlood - null donation!!");
            donation = null;
        }

        Optional<Clinic> donationClinicOptional = clinicRepository.findById(ClinicID);
        Clinic clinic = null;

        if(donationClinicOptional.isPresent())
            clinic = donationClinicOptional.get();
        else
            log.trace("createBlood - null clinic!!");

        Blood blood = bloodRepository.save(new Blood(collectionDate,quantity,state,type,donation,clinic));

        log.trace("createBlood - method exited");

        return blood;
    }

    @Override
    @Transactional
    public Optional<Blood> updateBlood(Long BloodID, Long collectionDate, Float quantity, Integer state, String type, Boolean tested,Boolean usable, Long DonationID, Long ClinicID)
    {
        Optional<Donation> donationOptional = donationRepository.findById(DonationID);
        Donation donation = null;

        if(donationOptional.isPresent())
            donation = donationOptional.get();

        Optional<Clinic> donationClinicOptional = clinicRepository.findById(ClinicID);
        Clinic clinic = null;

        if(donationClinicOptional.isPresent())
            clinic = donationClinicOptional.get();

        Optional<Blood> optionalBlood = bloodRepository.findById(BloodID);

        Donation finalDonation = donation; // werid java Quirks 101
        Clinic finalClinic = clinic; // same
        optionalBlood.ifPresent(st -> {
            st.setCollectionDate(collectionDate);
            st.setQuantity(quantity);
            st.setState(state);
            st.setType(type);
            st.setDonation(finalDonation);
            st.setClinic(finalClinic);
            st.setTested(tested);
            st.setUsable(usable);
        });

        return optionalBlood;
    }

    @Override
    public void deleteBlood(Long id) {
        log.trace("deleteBlood: id={}", id);

        bloodRepository.deleteById(id);

        log.trace("deleteBlood --- method finished");
    }


}
