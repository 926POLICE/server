package ro.ubb.catalog.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.ubb.catalog.core.model.Blood;
import ro.ubb.catalog.core.repository.BloodRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BloodServiceImpl implements BloodService
{
    private static final Logger log = LoggerFactory.getLogger(BloodServiceImpl.class);

    @Autowired
    private BloodRepository bloodRepository;

    @Override
    public List<Blood> getAllBloods() {
        log.trace("getAllBloods --- method entered");

        List<Blood> bloods = bloodRepository.findAll();

        log.trace("getAllBloods: bloods={}", bloods);

        return bloods;
    }

    @Override
    public Blood createBlood(String a, float b, int c, String d) {
        Blood blood = bloodRepository.save(new Blood(a,b,c,d));
        return blood;
    }

    @Override
    @Transactional
    public Optional<Blood> updateBlood(Long bloodId, String a, float b, int c, String d) {
        Optional<Blood> optionalBlood = bloodRepository.findById(bloodId);

        optionalBlood.ifPresent(st -> {
            st.setCollectionDate(a);
            st.setQuantity(b);
            st.setState(c);
            st.setType(d);
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
