package kpu.krosno.MediPets.repositories;

import kpu.krosno.MediPets.models.Pets;
import kpu.krosno.MediPets.models.Visits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface VisitsRepository extends JpaRepository<Visits, Long> {
    List<Visits> findAllByPetIdIn(List<Pets> petIds);
    Visits findByVisitId(Long visitId);
    List<Visits> findAllByVisitId(Long visitId);
    List<Visits> findAllByVisitDate(Date visitDate);
    List<Visits> findAllByVisitDateBefore(Date visitDate);
    List<Visits> findAllByVisitDateAfter(Date visitDate);
    List<Visits> findAllByVisitPrice(Double visitPrice);
    List<Visits> findAllByVisitPriceLessThan(Double visitPrice);
    List<Visits> findAllByVisitPriceGreaterThan(Double visitPrice);
    List<Visits> findAllByVisitStatusContaining(String visitStatus);
    List<Visits> findAllByIsPaid(Boolean isPaid);
}
