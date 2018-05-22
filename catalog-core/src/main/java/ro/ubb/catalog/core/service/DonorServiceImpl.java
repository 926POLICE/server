package ro.ubb.catalog.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.ubb.catalog.core.model.Donor;
import ro.ubb.catalog.core.repository.DonorRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public Donor createDonor(String username, String password,String name,Long birthday,String residence,String address,String bloodType,Boolean Rh,String anticorps,Boolean isDonor,Double latitude,Double longitude) {
        Donor donor = donorRepository.save(new Donor(name,birthday,residence,address,bloodType,Rh,anticorps,isDonor,latitude,longitude,username,password));
        return donor;
    }

    @Override
    @Transactional
    public Optional<Donor> updateDonor(Long DonorID, String name,Long birthday,String residence,String address,String bloodType,Boolean Rh,String anticorps,Boolean isDonor,Double latitude,Double longitude, Boolean eligibility, Long nextDonation, String username, String password) {
        Optional<Donor> optionalDonor = donorRepository.findById(DonorID);

        optionalDonor.ifPresent(st -> {
            st.setName(name);
            st.setBirthday(birthday);
            st.setResidence(residence);
            st.setAddress(address);
            st.setBloodType(bloodType);
            st.setRh(Rh);
            st.setAnticorps(anticorps);
            st.setIsDonor(isDonor);
            st.setLatitude(latitude);
            st.setLongitude(longitude);
            st.setEligibility(eligibility);
            st.setNextDonation(nextDonation);
            st.setUsername(username);
            st.setPassword(password);
        });

        return optionalDonor;
    }

    @Override
    @Transactional
    public Optional<Donor> setEligibility(Long DonorID,Boolean eligibility) {
        Optional<Donor> optionalDonor = donorRepository.findById(DonorID);

        optionalDonor.ifPresent(st -> {
            st.setEligibility(eligibility);
        });

        return optionalDonor;
    }

    @Override
    public Boolean notifyDonorsNeeded(String BloodType, Boolean RH, String Anticorps) {
        List<Donor> donors = this.getAllDonors();
        donors = donors.stream().filter(donor -> donor.getNextDonation() <= Instant.now().getEpochSecond() && donor.getAnticorps()==Anticorps && donor.getRh() == RH && donor.getBloodType()==BloodType).collect(Collectors.toList());
        Boolean res = false;
        if(donors.size()>0)
            res = true;
        donors.forEach(d->d.setHasBeenNotified(true));
        return res;
    }

    @Override
    public void deleteDonor(Long id) {
        log.trace("deleteDonor: id={}", id);

        donorRepository.deleteById(id);

        log.trace("deleteDonor --- method finished");
    }

    @Override
    public Optional<Donor> findbyID(Long DonorID)
    {
     return donorRepository.findById(DonorID);
    }
}
