package ro.ubb.catalog.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.ubb.catalog.core.model.Blood;
import ro.ubb.catalog.core.model.Clinic;
import ro.ubb.catalog.core.model.Donation;
import ro.ubb.catalog.core.repository.BloodRepository;
import ro.ubb.catalog.core.repository.ClinicRepository;
import ro.ubb.catalog.core.repository.DonationRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ClinicServiceImpl implements ClinicService {
    private static final Logger log = LoggerFactory.getLogger(DoctorServiceImpl.class);

    @Autowired
    private ClinicRepository clinicRepository;

    @Override
    public Clinic getTheClinic()
    {
        try
        {
            return clinicRepository.findAll().get(0);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Something's off with the singleton clinic: "+e.getMessage());
        }
    }
}
