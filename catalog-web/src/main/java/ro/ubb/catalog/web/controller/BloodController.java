package ro.ubb.catalog.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.ubb.catalog.core.model.Blood;
import ro.ubb.catalog.core.service.BloodService;
import ro.ubb.catalog.core.service.ClinicService;
import ro.ubb.catalog.web.converter.BloodConverter;
import ro.ubb.catalog.web.dto.BloodDTO;
import ro.ubb.catalog.web.dto.EmptyJsonResponse;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by radu.
 */


// For testing purposes ONLY.
@RestController
public class BloodController {

    private static final Logger log = LoggerFactory.getLogger(BloodController.class);

    @Autowired
    private BloodService bloodService;

    @Autowired
    private BloodConverter bloodConverter;

    @Autowired
    private ClinicService clinicService;

    @RequestMapping(value = "/bloods", method = RequestMethod.GET)
    public Set<BloodDTO> getBloodsList() {
        log.trace("getBloods");

        try
        {
            // Used for debugging.
            log.trace("BEGIN DEBUG");
            log.trace(clinicService.getTheClinic().getBloodStock().toString());
            log.trace("END DEBUG");
        }
        catch (Exception e)
        {
            log.trace("DEBUG FAIL: "+e.getMessage());
        }

        List<Blood> bloods = bloodService.getAllBloods();

        log.trace("getBloods: bloods={}", bloods);

        BloodConverter c = new BloodConverter();
        return c.convertModelsToDtos(bloods);
    }


    @RequestMapping(value = "/bloods/{bloodId}", method = RequestMethod.PUT)
    public BloodDTO updateBlood(@PathVariable final Long bloodId, @RequestBody final BloodDTO bloodDto)
    {
        log.trace("updateBlood: bloodId={}, bloodDtoMap={}", bloodId, bloodDto);

        Optional<Blood> bloodOptional = bloodService.updateBlood(bloodId,bloodDto.getCollectionDate(),bloodDto.getQuantity(),bloodDto.getState(),bloodDto.getType(),bloodDto.getTested(),bloodDto.getDonationID(),bloodDto.getClinicID());

        Map<String, BloodDTO> result = new HashMap<>();
        bloodOptional.ifPresent(blood -> result.put("blood", bloodConverter.convertModelToDto(blood)));

        log.trace("updateBlood: result={}", result);

        return result.get("blood");
    }


    @RequestMapping(value = "/bloods", method = RequestMethod.POST)
    public BloodDTO createBlood(@RequestBody final BloodDTO bloodDto) {
        log.trace("createBlood: bloodDtoMap={}", bloodDto);

        Blood blood = bloodService.createBlood(bloodDto.getCollectionDate(),bloodDto.getQuantity(),bloodDto.getState(),bloodDto.getType(),bloodDto.getDonationID(),bloodDto.getClinicID());

        BloodDTO result = bloodConverter.convertModelToDto(blood);

        log.trace("createBlood: result={}", result);
        return result;
    }


    @RequestMapping(value = "/bloods/{bloodId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteBlood(@PathVariable final Long bloodId) {
        log.trace("deleteBlood: bloodId={}", bloodId);

        bloodService.deleteBlood(bloodId);

        log.trace("deleteBlood - method end");

        return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.OK);
    }
}
