package ro.ubb.catalog.core.service;

import com.sun.org.apache.xpath.internal.operations.Bool;
import ro.ubb.catalog.core.model.Donor;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface DonorService
{
    List<Donor> getAllDonors();

    Donor createDonor(String username, String password, String name,String birthday,String residence,String bloodType,Boolean Rh,String anticorps,Boolean isDonor,Double latitude,Double longitude);

    Optional<Donor> updateDonor(Long DonorID, String name,String birthday,String residence,String bloodType,Boolean Rh,String anticorps,Boolean isDonor,Double latitude,Double longitude, Boolean eligibility, Long nextDonation, String username, String password);

    Optional<Donor> findbyID(Long DonorID);

    void deleteDonor(Long id);

    Optional<Donor> setEligibility(Long DonorID,Boolean eligibility);

    Boolean notifyDonorsNeeded(String BloodType,Boolean RH,final String Anticorps);
}
