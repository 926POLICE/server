package ro.ubb.catalog.core.service;

import com.sun.org.apache.xpath.internal.operations.Bool;
import ro.ubb.catalog.core.model.Blood;

import java.util.List;
import java.util.Optional;

public interface BloodService
{
    List<Blood> getAllBloods();

    List<Blood> getUsableBloods();

    List<Blood> getUnusableBloods();

    List<Blood> getUntestedBloods();

    Blood createBlood(Long collectionDate, Float quantity, Integer state, String type, Long DonationID, Long ClinicID);

    Optional<Blood> updateBlood(Long BloodID, Long collectionDate, Float quantity, Integer state, String type,Boolean tested,Boolean usable, Long DonationID, Long ClinicID);

    void deleteBlood(Long id);

    Optional<Blood> findByID(Long id);

    Optional<Blood> testBlood (Long BloodID, Boolean tested);

    Float checkAvailability(Float R, Float P, Float T);

    void honorRequest(Float R, Float P, Float T);

    void useBlood(Long bloodId);
}
