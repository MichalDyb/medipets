package kpu.krosno.MediPets.repositories;

import kpu.krosno.MediPets.models.Accounts;
import kpu.krosno.MediPets.models.Cities;
import kpu.krosno.MediPets.models.Persons;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonsRepository extends JpaRepository<Persons, Long> {
    List<Persons> findAllByIsStandardAccount(Boolean isStandardAccount);
    List<Persons> findAllByPersonId(Long personId);
    List<Persons> findAllByIsStandardAccountAndPersonId(Boolean isStandardAccount, Long personId);
    List<Persons> findAllByIsStandardAccountAndNameContaining(Boolean isStandardAccount, String name);
    List<Persons> findAllByIsStandardAccountAndSurnameContaining(Boolean isStandardAccount, String surname);
    List<Persons> findAllByIsStandardAccountAndAccountIdIn(Boolean isStandardAccount, List<Accounts> accounts);
    List<Persons> findAllByIsStandardAccountAndPeselContaining(Boolean isStandardAccount, String pesel);
    List<Persons> findAllByIsStandardAccountAndTelephoneContaining(Boolean isStandardAccount, String telephone);
    List<Persons> findAllByIsStandardAccountAndCityIdIn(Boolean isStandardAccount, List<Cities> cities);
    List<Persons> findAllByIsStandardAccountAndAddressContaining(Boolean isStandardAccount, String address);
    Persons findByPersonId(Long personId);
    Persons findByIsStandardAccountAndPersonId(Boolean isStandardAccount, Long personId);
}
