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
public class PersonnelServiceImpl implements PersonnelService
{
    private static final Logger log = LoggerFactory.getLogger(PersonnelServiceImpl.class);

    @Autowired
    private PersonnelRepository personnelRepository;

    @Override
    public List<Personnel> getAllPersonnel() {
        return personnelRepository.findAll();
    }
}
