package ro.ubb.catalog.core.service;

import ro.ubb.catalog.core.model.Patient;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface PatientService
{
    List<Patient> getAllPatients();

    Patient createPatient(String name,Long birthday,String residence,String address,String bloodType,Boolean Rh,String anticorps,Boolean isDonor,Float latitude,Float longitude, String hospital);

    Optional<Patient> updatePatient(Long patientId, String name,Long birthday,String residence,String address,String bloodType,Boolean Rh,String anticorps,Boolean isDonor,Float latitude,Float longitude, String hospital);

    void deletePatient(Long id);

    Optional <Patient> findByID(Long id);
}
