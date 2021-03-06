package ro.ubb.catalog.core.service;

import com.sun.org.apache.xpath.internal.operations.Bool;
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

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

    private Long currentTime = Instant.now().getEpochSecond();

    @Override
    public List<Blood> getAllBloods() {
        log.trace("getAllBloods --- method entered");

        List<Blood> bloods = bloodRepository.findAll();

        log.trace("getAllBloods: bloods={}", bloods);

        return bloods;
    }

    @Override
    public List<Blood> getUsableBloods() {
        List<Blood> bloodList = getAllBloods();
        bloodList = bloodList.stream().filter(b->b.getTested()==true && b.getUsable()==true && b.getCollectionDate()+86400*b.getShelfLife()>= currentTime && b.getState()!=3).collect(Collectors.toList());
        return bloodList;
    }

    @Override
    public List<Blood> getUnusableBloods() {
        Set<Blood> to_return = new HashSet<>();
        List<Blood> bloodList = getAllBloods();
        List<Blood> unusableBlood = bloodList.stream().filter(b->b.getUsable()==false).collect(Collectors.toList());
        List<Blood> expiredBlood = bloodList.stream().filter(b->b.getCollectionDate()+86400*b.getShelfLife()< currentTime).collect(Collectors.toList());
        to_return.addAll(unusableBlood);
        to_return.addAll(expiredBlood);
        return to_return.stream().collect(Collectors.toList());
    }

    @Override
    public List<Blood> getUntestedBloods() {
        return getAllBloods().stream().filter(p->p.getTested()==false).collect(Collectors.toList());
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
            log.trace("createBlood - null donation!!");

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

    @Override
    public Optional<Blood> findByID(Long id) {
        return bloodRepository.findById(id);
    }

    @Override
    @Transactional
    public Optional<Blood> testBlood(Long BloodID, Boolean flag) {
        Optional<Blood> optionalBlood = bloodRepository.findById(BloodID);

        optionalBlood.ifPresent(st -> {
            st.setState(2);
            st.setTested(true);
            st.setUsable(flag);
        });

        return optionalBlood;
    }

    @Override
    public Float checkAvailability(Float R, Float P, Float T) {
        List<Blood> bloodList = getUsableBloods();
        Float RAvailable = bloodList.stream().filter(p->p.getType()=="r").map(p->p.getQuantity()).reduce(0f,(a,b)->a+b).floatValue();
        Float PAvailable = bloodList.stream().filter(p->p.getType()=="p").map(p->p.getQuantity()).reduce(0f,(a,b)->a+b).floatValue();
        Float TAvailable = bloodList.stream().filter(p->p.getType()=="t").map(p->p.getQuantity()).reduce(0f,(a,b)->a+b).floatValue();
        if(RAvailable>=R && PAvailable >= P && TAvailable>=T)
            return 0f;
        else
            return R - RAvailable + P - PAvailable + T - TAvailable;
    }

    @Override
    public void honorRequest(Float R, Float P, Float T)
    {
        Float RNeeded = R, PNeeded = P, TNeeded = T;
        List<Blood> RbloodList = getUsableBloods().stream().filter(b->b.getType()=="r").collect(Collectors.toList());;
        List<Blood> PbloodList = getUsableBloods().stream().filter(b->b.getType()=="p").collect(Collectors.toList());;
        List<Blood> TbloodList = getUsableBloods().stream().filter(b->b.getType()=="t").collect(Collectors.toList());;
        for(int i = 0 ; i<RbloodList.size() && RNeeded > 0; i++)
        {
            RNeeded = RNeeded - RbloodList.get(i).getQuantity();
            RbloodList.get(i).setState(3);
        }
        for(int i = 0 ; i<PbloodList.size() && PNeeded > 0; i++)
        {
            PNeeded = PNeeded - PbloodList.get(i).getQuantity();
            PbloodList.get(i).setState(3);
        }
        for(int i = 0 ; i<TbloodList.size() && TNeeded > 0; i++)
        {
            TNeeded = TNeeded - TbloodList.get(i).getQuantity();
            TbloodList.get(i).setState(3);
        }
    }

    @Override
    public void useBlood(Long bloodId) {
        Optional<Blood> optionalBlood = bloodRepository.findById(bloodId);

        optionalBlood.ifPresent(st -> {
            st.setState(3);
        });
    }


}
