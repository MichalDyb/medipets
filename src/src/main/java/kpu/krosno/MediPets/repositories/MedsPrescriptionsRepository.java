package kpu.krosno.MediPets.repositories;

import kpu.krosno.MediPets.models.MedsPrescriptions;
import kpu.krosno.MediPets.models.MedsPrescriptionsKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedsPrescriptionsRepository extends JpaRepository<MedsPrescriptions, MedsPrescriptionsKey> {
}
