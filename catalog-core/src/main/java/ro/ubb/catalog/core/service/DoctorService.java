package ro.ubb.catalog.core.service;

import ro.ubb.catalog.core.model.Doctor;

import java.util.List;
import java.util.Optional;

public interface DoctorService
{
    List<Doctor> getAllDoctors();

    Doctor createDoctor(String name, String hospital);

    Optional<Doctor> updateDoctor(Long doctorId, String name, String hospital);

    void deleteDoctor(Long id);
}
