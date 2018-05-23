package ro.ubb.catalog.core.service;

import com.sun.org.apache.xpath.internal.operations.Bool;
import ro.ubb.catalog.core.model.Donor;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface DonorService
{
    List<Donor> getAllDonors();

    Donor createDonor(String username, String password, String name,Long birthday,String residence,String address, String bloodType,Boolean Rh,String anticorps,Boolean isDonor,Double latitude,Double longitude);

    Optional<Donor> updateDonor(Long DonorID, String name,Long birthday,String residence,String address, String bloodType,Boolean Rh,String anticorps,Boolean isDonor,Double latitude,Double longitude, Boolean eligibility, Long nextDonation, String username, String password);

    Optional<Donor> findbyID(Long DonorID);

    void deleteDonor(Long id);

    Optional<Donor> setEligibility(Long DonorID,Boolean eligibility);

    Optional<Donor> setInfo(Long DonorID,String bloodType, Boolean rh, String anticorps);

    Boolean notifyDonorsNeeded(String BloodType,Boolean RH,final String Anticorps);
}
