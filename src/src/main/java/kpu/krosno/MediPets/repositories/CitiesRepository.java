package kpu.krosno.MediPets.repositories;

import kpu.krosno.MediPets.models.Cities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CitiesRepository extends JpaRepository<Cities, Long> {
    Integer countByPostcode(String postCode);
    List<Cities> findAllByPostcode(String postCode);
    Integer countByCity(String city);
    List<Cities> findAllByCity(String city);
    List<Cities> findAllByPostcodeAndCity(String postCode, String city);
    List<Cities> findAllByPostcodeContaining(String postcode);
    List<Cities> findAllByCityContaining(String city);
}
