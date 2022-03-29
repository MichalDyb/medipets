package kpu.krosno.MediPets.repositories;

import kpu.krosno.MediPets.models.Meds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedsRepository extends JpaRepository<Meds, Long> {
    List<Meds> findAllByMedicine(String medicine);
    List<Meds> findAllByMedicineId(Long medicineId);
    List<Meds> findAllByMedicineContaining(String medicine);
    List<Meds> findAllByMedicinePrice(Double medicinePrice);
    List<Meds> findAllByMedicinePriceLessThan(Double medicinePrice);
    List<Meds> findAllByMedicinePriceGreaterThan(Double medicinePrice);
    Meds findByMedicineId(Long medicineId);
}
