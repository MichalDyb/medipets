package kpu.krosno.MediPets.repositories;

import kpu.krosno.MediPets.models.Species;
import kpu.krosno.MediPets.models.TreatmentsSpecies;
import kpu.krosno.MediPets.models.TreatmentsSpeciesKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TreatmentsSpeciesRepository extends JpaRepository<TreatmentsSpecies, TreatmentsSpeciesKey> {
    List<TreatmentsSpecies> findAllBySpeciesIdIn(List<Species> species);
}
