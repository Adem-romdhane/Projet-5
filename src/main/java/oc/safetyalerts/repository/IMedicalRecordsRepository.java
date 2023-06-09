package oc.safetyalerts.repository;

import oc.safetyalerts.model.MedicalRecords;

import java.util.List;
import java.util.Optional;

public interface IMedicalRecordsRepository {

    MedicalRecords findByFirstNameAndLastName(String firstName, String lastName);

    MedicalRecords save(MedicalRecords updateMedical);


    Optional<MedicalRecords> findById(Long id);

    List<MedicalRecords> findAll();

    void delete(MedicalRecords medicalRecord);
}
