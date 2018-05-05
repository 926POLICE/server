package ro.ubb.catalog.core.service;

import ro.ubb.catalog.core.model.Clinic;

import java.util.List;

public interface ClinicService
{
    List<Clinic> getAllClinics();

    Clinic createClinic(Double latitude, Double longitude);

    void deleteClinic(Long id);
}
