package ro.ubb.catalog.core.service;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.ubb.catalog.core.model.*;
import ro.ubb.catalog.core.repository.BloodRepository;
import ro.ubb.catalog.core.repository.ClinicRepository;
import ro.ubb.catalog.core.repository.DonationRepository;

import java.time.Instant;
import java.util.*;
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
        bloodList = bloodList.stream().filter(b-> b.getTested() && b.getUsable() && b.getCollectionDate()+86400*b.getShelfLife()>= currentTime && b.getState()!=3).collect(Collectors.toList());
        // only needs to return blood containres that haven't expired and that haven't been marked as ready to ship to the hospitals
        Collections.sort(bloodList);
        return bloodList;
    }

    @Override
    public List<Blood> getUnusableBloods() {
        Set<Blood> to_return = new HashSet<>();
        List<Blood> bloodList = getAllBloods();
        List<Blood> unusableBlood = bloodList.stream().filter(b-> !b.getUsable() && b.getState()!=4).collect(Collectors.toList());
        List<Blood> expiredBlood = bloodList.stream().filter(b->b.getCollectionDate()+86400*b.getShelfLife()< currentTime).collect(Collectors.toList());
        to_return.addAll(unusableBlood);
        to_return.addAll(expiredBlood);

        return to_return.stream().collect(Collectors.toList());
    }

    @Override
    public List<Blood> getUntestedBloods() {
        return getAllBloods().stream().filter(p-> !p.getTested()).collect(Collectors.toList());
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
    @Transactional
    public Optional<Blood> removeBlood(Long BloodID) {
        Optional<Blood> optionalBlood = bloodRepository.findById(BloodID);

        optionalBlood.ifPresent(st -> {
            st.setState(4);
        });

        return optionalBlood;
    }

    @Override
    public Float checkAvailability(Request request)
    {
        Patient patient = request.getPatient();

        Float R = request.getRQuantity(), P = request.getPQuantity(), T = request.getTQuantity();
        List<Blood> bloodList = getUsableBloods().stream().filter(b-> b.getDonation().getDonor().isCompatible(patient)).collect(Collectors.toList());
        Float RAvailable = bloodList.stream().filter(p -> p.getType().equals("r")).map(Blood::getQuantity).reduce(0f, (a, b) -> a + b);
        Float PAvailable = bloodList.stream().filter(p -> p.getType().equals("p")).map(Blood::getQuantity).reduce(0f, (a, b) -> a + b);
        Float TAvailable = bloodList.stream().filter(p -> p.getType().equals("t")).map(Blood::getQuantity).reduce(0f, (a, b) -> a + b);
        if(RAvailable>=R && PAvailable >= P && TAvailable>=T)
            return 0f;
        else
            return R - RAvailable + P - PAvailable + T - TAvailable;
    }

    @Override
    @Transactional
    public void honorRequest(Request r)
    {
        log.trace("honorRequest entered!");

        Patient patient = r.getPatient();

        Float RNeeded = r.getRQuantity(), PNeeded = r.getPQuantity(), TNeeded = r.getTQuantity();
        List<Blood> RbloodList = getUsableBloods().stream().filter(b-> b.getType().equals("r") && b.getDonation().getDonor().isCompatible(patient)).collect(Collectors.toList());;
        List<Blood> PbloodList = getUsableBloods().stream().filter(b-> b.getType().equals("p")&& b.getDonation().getDonor().isCompatible(patient)).collect(Collectors.toList());;
        List<Blood> TbloodList = getUsableBloods().stream().filter(b-> b.getType().equals("t")&& b.getDonation().getDonor().isCompatible(patient)).collect(Collectors.toList());;

        for(int i = 0 ; i<RbloodList.size() && RNeeded > 0; i++)
        {
            RNeeded = RNeeded - RbloodList.get(i).getQuantity();
            useBlood(RbloodList.get(i).getId());
        }
        for(int i = 0 ; i<PbloodList.size() && PNeeded > 0; i++)
        {
            PNeeded = PNeeded - PbloodList.get(i).getQuantity();
            useBlood(PbloodList.get(i).getId());
        }
        for(int i = 0 ; i<TbloodList.size() && TNeeded > 0; i++)
        {
            TNeeded = TNeeded - TbloodList.get(i).getQuantity();
            useBlood(TbloodList.get(i).getId());
        }

        log.trace("honorRequest exited!");
    }

    @Override
    @Transactional
    public void useBlood(Long bloodId) {
        Optional<Blood> optionalBlood = bloodRepository.findById(bloodId);

        optionalBlood.ifPresent(st -> {
            st.setState(3);
        });
    }


}
