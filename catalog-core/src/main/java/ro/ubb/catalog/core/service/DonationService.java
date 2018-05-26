package ro.ubb.catalog.core.service;

import ro.ubb.catalog.core.model.*;

import java.util.List;
import java.util.Optional;

public interface DonationService
{
    List<Donation> getAllDonations();

    Donation createDonation(Long donorID, Long patientID, Long donationClinicID);

    Optional<Donation> updateDonation(Long DonationID, Long RBloodID, Long PBloodID, Long TBloodID, Long donorID, Boolean analysisResult, Long patientID, Long donationClinicID);

    void deleteDonation(Long id);

    Optional<Donation> findByID(Long id);
}
