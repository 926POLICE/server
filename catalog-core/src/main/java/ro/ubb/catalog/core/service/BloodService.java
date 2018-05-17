package ro.ubb.catalog.core.service;

import ro.ubb.catalog.core.model.Blood;

import java.util.List;
import java.util.Optional;

public interface BloodService
{
    List<Blood> getAllBloods();

    Blood createBlood(String collectionDate, Float quantity, Integer state, String type, Long DonationID, Long ClinicID);

    Optional<Blood> updateBlood(Long BloodID, String collectionDate, Float quantity, Integer state, String type,Boolean tested, Long DonationID, Long ClinicID);

    void deleteBlood(Long id);
}
