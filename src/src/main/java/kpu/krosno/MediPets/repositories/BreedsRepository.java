package kpu.krosno.MediPets.repositories;

import kpu.krosno.MediPets.models.Breeds;
import kpu.krosno.MediPets.models.Pets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BreedsRepository extends JpaRepository<Breeds, Long> {
    List<Breeds> findAllByBreed(String breed);
    List<Breeds> findAllByBreedContaining(String breed);
    List<Breeds> findAllByBreedId(Long breedId);
    Breeds findByBreedId(Long breedId);
}
