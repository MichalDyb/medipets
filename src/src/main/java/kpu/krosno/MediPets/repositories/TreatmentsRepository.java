package kpu.krosno.MediPets.repositories;

import kpu.krosno.MediPets.models.Species;
import kpu.krosno.MediPets.models.Treatments;
import kpu.krosno.MediPets.models.TreatmentsSpecies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface TreatmentsRepository extends JpaRepository<Treatments, Long> {
    List<Treatments> findByTreatment(String treatment);
    Treatments findByTreatmentId(Long treatmentId);
    List<Treatments> findAllByTreatmentId(Long treatmentId);
    List<Treatments> findAllByTreatmentContaining(String treatment);
    List<Treatments> findAllByTreatmentPrice(Double treatmentPrice);
    List<Treatments> findAllByTreatmentPriceLessThan(Double treatmentPrice);
    List<Treatments> findAllByTreatmentPriceGreaterThan(Double treatmentPrice);
    List<Treatments> findAllByTreatmentIdIn(List<Long> treatmentsSpecies);
}
