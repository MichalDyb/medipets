package kpu.krosno.MediPets.utility;

import kpu.krosno.MediPets.models.*;
import kpu.krosno.MediPets.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataLoader implements ApplicationRunner {
    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private AccountsRepository accountsRepository;
    @Autowired
    private AccountsRolesRepository accountsRolesRepository;
    @Autowired
    private SpeciesRepository speciesRepository;
    @Autowired
    private BreedsRepository breedsRepository;
    @Autowired
    private CitiesRepository citiesRepository;
    @Autowired
    private PersonsRepository personsRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Roles role1, role2, role3;
        role1 = new Roles();
        role2 = new Roles();
        role3 = new Roles();
        role1.setRole("ROLE_USER");
        role2.setRole("ROLE_STAFF");
        role3.setRole("ROLE_SUPERUSER");
        rolesRepository.saveAll(Arrays.asList(role1, role2, role3));
        Accounts account = new Accounts();
        account.setEmail("root");
        account.setPassword(new BCryptPasswordEncoder().encode("toor"));
        account.setExpired(false);
        account.setCredentialsExpired(false);
        account.setEnabled(true);
        account.setLocked(false);
        accountsRepository.save(account);
        AccountsRoles accountsRole = new AccountsRoles();
        accountsRole.setRoleId(role3);
        accountsRole.setAccountId(account);
        accountsRolesRepository.save(accountsRole);
        account.getAccountsRoles().add(accountsRole);
        accountsRepository.save(account);
        role3.getAccountsRoles().add(accountsRole);
        rolesRepository.save(role3);
        Breeds breed = new Breeds();
        Species species = new Species();
        breed.setBreed("Owczarek niemiecki");
        breed.setPets(null);
        species.setSpecies("Pies");
        species.setPets(null);
        species.setTreatmentsSpecies(null);
        breedsRepository.save(breed);
        speciesRepository.save(species);
        Cities city = new Cities();
        city.setCity("Miasto");
        city.setPostcode("12-345");
        citiesRepository.save(city);
        Persons person = new Persons();
        person.setCityId(city);
        person.setTelephone("123456789");
        person.setAddress("RootWay 1/0");
        person.setPesel("00000000000");
        person.setAccountId(account);
        person.setName("System");
        person.setSurname("Administrator");
        person.setCityId(city);
        person.setIsStandardAccount(false);
        person.setPets(null);
        person.setPrescriptionsClients(null);
        person.setPrescriptionsDoctors(null);
        personsRepository.save(person);
        account.setPersonId(person);
        accountsRepository.save(account);
    }
}
