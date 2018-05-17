package ro.ubb.catalog.core.service;

import ro.ubb.catalog.core.model.Doctor;

import java.util.List;
import java.util.Optional;

public interface DoctorService
{
    List<Doctor> getAllDoctors();

    Doctor createDoctor(String username, String password, String name, String hospital);

    Optional<Doctor> updateDoctor(Long doctorId, String name, String hospital, String username, String password);

    void deleteDoctor(Long id);
}
