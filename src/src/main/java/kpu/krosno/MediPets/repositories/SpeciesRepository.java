package kpu.krosno.MediPets.repositories;

import kpu.krosno.MediPets.models.Species;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpeciesRepository extends JpaRepository<Species, Long> {
    List<Species> findAllBySpecies(String species);
    List<Species> findAllBySpeciesId(Long speciesId);
    List<Species> findAllBySpeciesContaining(String species);
    Species findBySpeciesId(Long speciesId);
}
