package kpu.krosno.MediPets.repositories;

import kpu.krosno.MediPets.models.TreatmentsVisits;
import kpu.krosno.MediPets.models.TreatmentsVisitsKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TreatmentsVisitsRepository extends JpaRepository<TreatmentsVisits, TreatmentsVisitsKey> {
}
