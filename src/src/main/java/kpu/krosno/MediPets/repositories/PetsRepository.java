package kpu.krosno.MediPets.repositories;

import kpu.krosno.MediPets.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PetsRepository extends JpaRepository<Pets, Long> {
    Pets findByPetId(Long petId);
    List<Pets> findAllByPersonId(Persons personId);
    List<Pets> findAllByPetId(Long petId);
    List<Pets> findAllByPetNameContaining(String petName);
    List<Pets> findAllByGenderContaining(String gender);
    List<Pets> findAllBySpeciesIdIn(List<Species> species);
    List<Pets> findAllByBreedIdIn(List<Breeds> breeds);
    List<Pets> findAllByDateOfBirth(Date dateOfBirth);
    List<Pets> findAllByDateOfBirthBefore(Date dateOfBirth);
    List<Pets> findAllByDateOfBirthAfter(Date dateOfBirth);
}
