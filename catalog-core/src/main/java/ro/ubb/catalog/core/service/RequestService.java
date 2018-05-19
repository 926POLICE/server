package ro.ubb.catalog.core.service;

import ro.ubb.catalog.core.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestService
{
    List<Request> getAllRequests();

    Request createRequest(Long patientID, Long doctorID, Float RQuantity, Float PQuantity, Float TQuantity, Boolean priority, Long clinicID);

    Optional<Request> updateRequest(Long requestID, Long patientID, Long doctorID, Float RQuantity, Float PQuantity, Float TQuantity, Boolean priority, Boolean completed, Long clinicID);

    void deleteRequest(Long id);

    Optional<Request> findByID(Long id);
}
