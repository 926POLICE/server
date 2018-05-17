package ro.ubb.catalog.core.service;

import ro.ubb.catalog.core.model.Donor;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface DonorService
{
    List<Donor> getAllDonors();

    Donor createDonor(String username, String password, String name,String birthday,String residence,String bloodType,Boolean Rh,String anticorps,Boolean isDonor,Double latitude,Double longitude);

    Optional<Donor> updateDonor(Long DonorID, String name,String birthday,String residence,String bloodType,Boolean Rh,String anticorps,Boolean isDonor,Double latitude,Double longitude, Boolean eligibility, String nextDonation, String username, String password);

    Optional<Donor> findbyID(Long DonorID);

    void deleteDonor(Long id);
}
