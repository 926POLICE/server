package ro.ubb.catalog.core.service;

import ro.ubb.catalog.core.model.Blood;

import java.util.List;
import java.util.Optional;

public interface BloodService
{
    List<Blood> getAllBloods();

    Blood createBlood(String a, float b, int c, String d);

    Optional<Blood> updateBlood(Long bloodId, String a, float b, int c, String d);

    void deleteBlood(Long id);
}
