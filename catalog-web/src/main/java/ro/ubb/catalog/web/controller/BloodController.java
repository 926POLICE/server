package ro.ubb.catalog.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.ubb.catalog.core.model.Blood;
import ro.ubb.catalog.core.model.BloodList;
import ro.ubb.catalog.core.service.BloodService;
import ro.ubb.catalog.web.dto.EmptyJsonResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by radu.
 */

@RestController
public class BloodController {

    private static final Logger log = LoggerFactory.getLogger(BloodController.class);

    @Autowired
    private BloodService bloodService;


    @RequestMapping(value = "/bloodStocks", method = RequestMethod.GET)
    public List<Blood> getBloodStocks()
    {
        log.trace("getBloods");

        List<Blood> bloods = bloodService.getAllBloods();

        log.trace("getBloods: bloods={}", bloods);

        return bloods;
    }


    @RequestMapping(value = "/bloods/{bloodId}", method = RequestMethod.PUT)
    public Blood updateBlood(
            @PathVariable final Long bloodId,
            @RequestBody final Blood to_update) {
        log.trace("updateBlood: bloodId={}, bloodDtoMap={}", bloodId, to_update);

        Optional<Blood> bloodOptional = bloodService.updateBlood(bloodId,
                to_update.getCollectionDate(),to_update.getQuantity(),to_update.getState(),to_update.getType());

        Map<String, Blood> result = new HashMap<>();
        bloodOptional.ifPresentOrElse(
                blood -> result.put("blood", blood),
                () -> result.put("blood", new Blood()));

        log.trace("updateBlood: result={}", result);

        return result.get("blood");
    }


    @RequestMapping(value = "/bloods", method = RequestMethod.POST)
    public Blood createBlood(
            @RequestBody final Blood to_create) {
        log.trace("createBlood: bloodDtoMap={}", to_create);

        Blood blood = bloodService.createBlood(
                to_create.getCollectionDate(),to_create.getQuantity(),to_create.getState(),to_create.getType());

        log.trace("createBlood: result={}", blood);
        return blood;
    }


    @RequestMapping(value = "bloods/{bloodId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteBlood(@PathVariable final Long bloodId) {
        log.trace("deleteBlood: bloodId={}", bloodId);

        bloodService.deleteBlood(bloodId);

        log.trace("deleteBlood - method end");

        return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.OK);
    }
}
