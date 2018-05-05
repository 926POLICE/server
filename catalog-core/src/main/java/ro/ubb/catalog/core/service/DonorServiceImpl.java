package ro.ubb.catalog.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.ubb.catalog.core.model.Donor;
import ro.ubb.catalog.core.repository.DonorRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DonorServiceImpl implements DonorService
{
    private static final Logger log = LoggerFactory.getLogger(DonorServiceImpl.class);

    @Autowired
    private DonorRepository donorRepository;

    @Override
    public List<Donor> getAllDonors() {
        log.trace("getAllDonors --- method entered");

        List<Donor> donors = donorRepository.findAll();

        log.trace("getAllDonors: donors={}", donors);

        return donors;
    }

    @Override
    public Donor createDonor(String name,String birthday,String residence,String bloodType,Boolean Rh,String anticorps,Boolean isDonor,Double latitude,Double longitude) {
        Donor donor = donorRepository.save(new Donor(name,birthday,residence,bloodType,Rh,anticorps,isDonor,latitude,longitude));
        return donor;
    }

    @Override
    @Transactional
    public Optional<Donor> updateDonor(Long DonorID, String name,String birthday,String residence,String bloodType,Boolean Rh,String anticorps,Boolean isDonor,Double latitude,Double longitude, Boolean eligibility, String nextDonation) {
        Optional<Donor> optionalDonor = donorRepository.findById(DonorID);

        optionalDonor.ifPresent(st -> {
            st.setName(name);
            st.setBirthday(birthday);
            st.setResidence(residence);
            st.setBloodType(bloodType);
            st.setRh(Rh);
            st.setAnticorps(anticorps);
            st.setIsDonor(isDonor);
            st.setLatitude(latitude);
            st.setLongitude(longitude);
            st.setEligibility(eligibility);
            st.setNextDonation(nextDonation);
        });

        return optionalDonor;
    }

    @Override
    public void deleteDonor(Long id) {
        log.trace("deleteDonor: id={}", id);

        donorRepository.deleteById(id);

        log.trace("deleteDonor --- method finished");
    }


}
