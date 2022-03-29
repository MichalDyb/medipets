package kpu.krosno.MediPets.repositories;

import kpu.krosno.MediPets.models.Persons;
import kpu.krosno.MediPets.models.Prescriptions;
import kpu.krosno.MediPets.models.Visits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PrescriptionsRepository extends JpaRepository<Prescriptions, Long> {
    Prescriptions findByPrescriptionId(Long prescriptionId);
    List<Prescriptions> findAllByClientId(Persons clientId);
    List<Prescriptions> findAllByDoctorId(Persons doctorId);
    List<Prescriptions> findAllByPrescriptionId(Long prescriptionId);
    List<Prescriptions> findAllByVisitId(Visits visitId);
    List<Prescriptions> findAllByPrescriptionDate(Date prescriptionDate);
    List<Prescriptions> findAllByPrescriptionDateBefore(Date prescriptionDate);
    List<Prescriptions> findAllByPrescriptionDateAfter(Date prescriptionDate);
}
