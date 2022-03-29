package kpu.krosno.MediPets.controllers;

import kpu.krosno.MediPets.models.*;
import kpu.krosno.MediPets.repositories.*;
import kpu.krosno.MediPets.security.AccountDetails;
import kpu.krosno.MediPets.utility.*;
import org.apache.catalina.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.EntityNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Controller
public class EmployeeController {

    @Autowired
    private SpeciesRepository speciesRepository;
    @Autowired
    private BreedsRepository breedsRepository;
    @Autowired
    private PersonsRepository personsRepository;
    @Autowired
    private PetsRepository petsRepository;
    @Autowired
    private VisitsRepository visitsRepository;
    @Autowired
    private AccountsRepository accountsRepository;
    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private AccountsRolesRepository accountsRolesRepository;
    @Autowired
    private CitiesRepository citiesRepository;
    @Autowired
    private PrescriptionsRepository prescriptionsRepository;
    @Autowired
    private MedsPrescriptionsRepository medsPrescriptionsRepository;
    @Autowired
    private MedsRepository medsRepository;
    @Autowired
    private TreatmentsRepository treatmentsRepository;
    @Autowired
    private TreatmentsVisitsRepository treatmentsVisitsRepository;

    // GET: Index
    @RequestMapping(value = "/employee", method = RequestMethod.GET)
    public String getIndex(Model model)
    {
        return "/employee/index";
    }

    // GET: Pets
    @RequestMapping(value = "/employee/pets", method = RequestMethod.GET)
    public String getPets(Model model)
    {
        model.addAttribute("pets", petsRepository.findAll());
        return "/employee/pets/index";
    }

    // GET: Search Pets
    @RequestMapping(value = "/employee/pets/search", method = RequestMethod.GET)
    public String getSearchPets(Model model)
    {
        model.addAttribute("searchData", new SearchData());
        return "/employee/pets/search";
    }

    // POST: Search Pets
    @RequestMapping(value = "/employee/pets/search", method = RequestMethod.POST)
    public String postSearchPets(Model model, SearchData searchData)
    {
        model.addAttribute("searchData", searchData);
        String search = "";
        if(searchData.getValue().isEmpty())
            search = "/all/all";
        else
            search = "/" + searchData.getOption() + "/" + searchData.getValue();
        return "redirect:/employee/pets/search" + search;
    }

    // GET: SearchScore Pets
    @RequestMapping(value = "/employee/pets/search/{option}/{value}", method = RequestMethod.GET)
    public String getSearchScorePets(@PathVariable(name = "option", value = "option") String option, @PathVariable(name = "value", value = "value") String value, Model model)
    {
        List<Pets> pets = new ArrayList<Pets>();
        if(option.equals("all"))
        {
            pets = petsRepository.findAll();
        }
        else if(option.equals("1"))
        {
            try {
                long id = Long.parseLong(value);
                pets = petsRepository.findAllByPetId(id);
            }
            catch (Exception ignored) {}
        }
        else if(option.equals("2"))
        {
            pets = petsRepository.findAllByPetNameContaining(value);
        }
        else if(option.equals("3"))
        {
            try {
                pets = petsRepository.findAllByDateOfBirth(new SimpleDateFormat("yyyy-M-dd").parse(value));
            }
            catch (Exception ignored) {}
        }
        else if(option.equals("4"))
        {
            try {
                pets = petsRepository.findAllByDateOfBirthBefore(new SimpleDateFormat("yyyy-M-dd").parse(value));
            }
            catch (Exception ignored) {}
        }
        else if(option.equals("5"))
        {
            try {
                pets = petsRepository.findAllByDateOfBirthAfter(new SimpleDateFormat("yyyy-M-dd").parse(value));
            }
            catch (Exception ignored) {}
        }
        else if(option.equals("6"))
        {
            pets = petsRepository.findAllByGenderContaining(value);
        }
        else if(option.equals("7"))
        {
            List<Species> species = speciesRepository.findAllBySpeciesContaining(value);
            pets = petsRepository.findAllBySpeciesIdIn(species);
        }
        else if(option.equals("8"))
        {
            List<Breeds> breeds = breedsRepository.findAllByBreedContaining(value);
            pets = petsRepository.findAllByBreedIdIn(breeds);
        }

        model.addAttribute("pets", pets);
        return "/employee/pets/index";
    }

    // GET: Add pet
    @RequestMapping(value = "/employee/pets/add", method = RequestMethod.GET)
    public String getAddPet(Model model)
    {
        List<Species> species = speciesRepository.findAll();
        List<Breeds> breeds = breedsRepository.findAll();
        List<Persons> persons = personsRepository.findAll();
        model.addAttribute("breeds", breeds);
        model.addAttribute("species", species);
        model.addAttribute("persons", persons);
        model.addAttribute("petData", new PetData());
        return "/employee/pets/add";
    }

    // POST: Add pet
    @RequestMapping(value = "/employee/pets/add", method = RequestMethod.POST)
    public String postAddPet(PetData petData, Model model) throws ParseException {
        if (CustomValidator.petNameValidation(model, petData.getPetName())
                || CustomValidator.dateOfBirthValidation(model, petData.getDateOfBirth())
                || CustomValidator.genderValidation(model, petData.getGender())
                || CustomValidator.petDescriptionValidation(model, petData.getPetDescription()))
        {
            List<Species> species = speciesRepository.findAll();
            List<Breeds> breeds = breedsRepository.findAll();
            List<Persons> persons = personsRepository.findAll();
            model.addAttribute("breeds", breeds);
            model.addAttribute("species", species);
            model.addAttribute("persons", persons);
            model.addAttribute("petData", petData);
            return "/employee/pets/add";
        }
        try {
            Pets pet = new Pets();
            pet.setPetName(petData.getPetName());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-dd");
            pet.setDateOfBirth(simpleDateFormat.parse(petData.getDateOfBirth()));
            pet.setPetDescription(petData.getPetDescription());
            pet.setGender(petData.getGender());
            Breeds breed = breedsRepository.findByBreedId(Long.parseLong(petData.getBreedId()));
            if (breed == null) throw new NumberFormatException("Brak");
            pet.setBreedId(breed);
            Persons person = personsRepository.findByPersonId(Long.parseLong(petData.getPersonId()));
            if (person == null) throw new NumberFormatException("Brak");
            pet.setPersonId(person);
            Species species = speciesRepository.findBySpeciesId(Long.parseLong(petData.getSpeciesId()));
            if (species == null) throw new NumberFormatException("Brak");
            pet.setSpeciesId(species);
            pet.setVisits(null);
            petsRepository.save(pet);
            breed = breedsRepository.findByBreedId(Long.parseLong(petData.getBreedId()));
            breed.getPets().add(pet);
            breedsRepository.save(breed);
            species = speciesRepository.findBySpeciesId(Long.parseLong(petData.getSpeciesId()));
            species.getPets().add(pet);
            speciesRepository.save(species);
            person = personsRepository.findByPersonId(Long.parseLong(petData.getPersonId()));
            person.getPets().add(pet);
            personsRepository.save(person);
        }
        catch (NumberFormatException exception)
        {
            return "redirect:/employee/pets";
        }
        return "redirect:/employee/pets";
    }

    // GET: Edit pet
    @RequestMapping(value = "/employee/pets/edit/{id}", method = RequestMethod.GET)
    public String getEditPet(@PathVariable(name = "id", value = "id") String id, Model model)
    {
        PetData petData = new PetData();
        Pets pet = null;
        try {
            pet = petsRepository.findByPetId(Long.parseLong(id));
            if (pet == null) throw new NumberFormatException("Brak");
        }
        catch (NumberFormatException exception)
        {
            return "redirect:/employee/pets";
        }
        petData.setPetId(pet.getPetId().toString());
        petData.setPetName(pet.getPetName());
        petData.setDateOfBirth(pet.getDateOfBirth().toString());
        petData.setPetDescription(pet.getPetDescription());
        petData.setGender(pet.getGender());
        petData.setPersonId(pet.getPersonId().getPersonId().toString());
        petData.setPetId(pet.getPetId().toString());
        petData.setBreedId(pet.getBreedId().getBreedId().toString());
        petData.setSpeciesId(pet.getSpeciesId().getSpeciesId().toString());
        List<Species> species = speciesRepository.findAll();
        List<Breeds> breeds = breedsRepository.findAll();
        List<Persons> persons = personsRepository.findAll();
        model.addAttribute("breeds", breeds);
        model.addAttribute("species", species);
        model.addAttribute("persons", persons);
        model.addAttribute("petData", petData);
        return "/employee/pets/edit";
    }

    // POST: Edit pet
    @RequestMapping(value = "/employee/pets/edit", method = RequestMethod.POST)
    public String postEditPet(PetData petData, Model model) throws ParseException {
        if (CustomValidator.petNameValidation(model, petData.getPetName())
                || CustomValidator.dateOfBirthValidation(model, petData.getDateOfBirth())
                || CustomValidator.genderValidation(model, petData.getGender())
                || CustomValidator.petDescriptionValidation(model, petData.getPetDescription()))
        {
            List<Species> species = speciesRepository.findAll();
            List<Breeds> breeds = breedsRepository.findAll();
            List<Persons> persons = personsRepository.findAll();
            model.addAttribute("breeds", breeds);
            model.addAttribute("species", species);
            model.addAttribute("persons", persons);
            model.addAttribute("petData", petData);
            model.addAttribute("petData", petData);
            return "/employee/pets/edit";
        }
        try {
            Pets pet = petsRepository.findByPetId(Long.parseLong(petData.getPetId()));
            if(pet == null) throw new NumberFormatException("Brak");
            if (pet.getBreedId().getBreedId() != Long.parseLong(petData.getBreedId()))
            {
                Breeds oldBreed = pet.getBreedId();
                Breeds breed = breedsRepository.findByBreedId(Long.parseLong(petData.getBreedId()));
                if(breed == null) throw new NumberFormatException("Brak");
                pet.setBreedId(breed);
                petsRepository.save(pet);
                oldBreed.getPets().remove(pet);
                breedsRepository.save(oldBreed);
                Breeds newBreed = breedsRepository.findByBreedId(Long.parseLong(petData.getBreedId()));
                if(newBreed == null) throw new NumberFormatException("Brak");
                newBreed.getPets().add(pet);
                breedsRepository.save(newBreed);
            }
            if (pet.getSpeciesId().getSpeciesId() != Long.parseLong(petData.getSpeciesId()))
            {
                Species oldSpecies = pet.getSpeciesId();
                Species species = speciesRepository.findBySpeciesId(Long.parseLong(petData.getSpeciesId()));
                if(species == null) throw new NumberFormatException("Brak");
                pet.setSpeciesId(species);
                petsRepository.save(pet);
                oldSpecies.getPets().remove(pet);
                speciesRepository.save(oldSpecies);
                Species newSpecies = speciesRepository.findBySpeciesId(Long.parseLong(petData.getSpeciesId()));
                if(newSpecies == null) throw new NumberFormatException("Brak");
                newSpecies.getPets().add(pet);
                speciesRepository.save(newSpecies);
            }
            if (pet.getPersonId().getPersonId() != Long.parseLong(petData.getPersonId()))
            {
                Persons oldPerson = pet.getPersonId();
                Persons person = personsRepository.findByPersonId(Long.parseLong(petData.getPersonId()));
                if(person == null) throw new NumberFormatException("Brak");
                pet.setPersonId(person);
                petsRepository.save(pet);
                oldPerson.getPets().remove(pet);
                personsRepository.save(oldPerson);
                Persons newPerson = personsRepository.findByPersonId(Long.parseLong(petData.getSpeciesId()));
                if(newPerson == null) throw new NumberFormatException("Brak");
                newPerson.getPets().add(pet);
                personsRepository.save(newPerson);
            }
            pet.setPetName(petData.getPetName());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-dd");
            pet.setDateOfBirth(simpleDateFormat.parse(petData.getDateOfBirth()));
            pet.setPetDescription(petData.getPetDescription());
            pet.setGender(petData.getGender());
            petsRepository.save(pet);
        }
        catch (NumberFormatException exception)
        {
            return "redirect:/employee/pets";
        }
        return "redirect:/employee/pets";
    }

    // GET: Pet details
    @RequestMapping(value = "/employee/pets/{id}", method = RequestMethod.GET)
    public String getPetDetails(@PathVariable(name = "id", value = "id") String id, Model model)
    {
        try {
            Pets pet = petsRepository.findByPetId(Long.parseLong(id));
            if(pet == null) throw new NumberFormatException("Brak");
            model.addAttribute("pet", pet);
        }
        catch (NumberFormatException exception)
        {
            return "redirect:/employee/pets";
        }
        return "/employee/pets/details";
    }

    // GET: Breeds
    @RequestMapping(value = "/employee/breeds", method = RequestMethod.GET)
    public String getBreeds(Model model)
    {
        model.addAttribute("breeds", breedsRepository.findAll());
        return "/employee/breeds/index";
    }

    // GET: Search Breeds
    @RequestMapping(value = "/employee/breeds/search", method = RequestMethod.GET)
    public String getSearchBreeds(Model model)
    {
        model.addAttribute("searchData", new SearchData());
        return "/employee/breeds/search";
    }

    // POST: Search Breeds
    @RequestMapping(value = "/employee/breeds/search", method = RequestMethod.POST)
    public String postSearchBreeds(Model model, SearchData searchData)
    {
        model.addAttribute("searchData", searchData);
        String search = "";
        if(searchData.getValue().isEmpty())
            search = "/all/all";
        else
            search = "/" + searchData.getOption() + "/" + searchData.getValue();
        return "redirect:/employee/breeds/search" + search;
    }

    // GET: SearchScore Breeds
    @RequestMapping(value = "/employee/breeds/search/{option}/{value}", method = RequestMethod.GET)
    public String getSearchScoreBreed(@PathVariable(name = "option", value = "option") String option, @PathVariable(name = "value", value = "value") String value, Model model)
    {
        List<Breeds> breeds = new ArrayList<Breeds>();
        if(option.equals("all"))
        {
            breeds = breedsRepository.findAll();
        }
        else if(option.equals("1"))
        {
            try {
                long id = Long.parseLong(value);
                breeds = breedsRepository.findAllByBreedId(id);
            }
            catch (Exception ignored) {}
        }
        else if(option.equals("2"))
        {
            breeds = breedsRepository.findAllByBreedContaining(value);
        }

        model.addAttribute("breeds", breeds);
        return "/employee/breeds/index";
    }

    // GET: Add breed
    @RequestMapping(value = "/employee/breeds/add", method = RequestMethod.GET)
    public String getAddBreed(Model model)
    {
        model.addAttribute("breedData", new BreedData());
        return "/employee/breeds/add";
    }

    // POST: Add breed
    @RequestMapping(value = "/employee/breeds/add", method = RequestMethod.POST)
    public String postAddBreed(BreedData breedData, Model model)
    {
        if (CustomValidator.breedValidation(model, breedData.getBreed()))
        {
            model.addAttribute("breedData", breedData);
            return "/employee/breeds/add";
        }
        if(!breedsRepository.findAllByBreed(breedData.getBreed()).isEmpty())
        {
            model.addAttribute("breedError", "Taka rasa już została dodana.");
            return "/employee/breeds/add";
        }
        Breeds breed = new Breeds();
        breed.setBreed(breedData.getBreed());
        breedsRepository.save(breed);
        return "redirect:/employee/breeds";
    }

    // GET: Edit breed
    @RequestMapping(value = "/employee/breeds/edit/{id}", method = RequestMethod.GET)
    public String getEditBreed(@PathVariable(name = "id", value = "id") String id, Model model)
    {
        Breeds breed = null;
        try {
            breed = breedsRepository.findByBreedId(Long.parseLong(id));
            if(breed == null) throw new NumberFormatException("Brak");
        }
        catch (NumberFormatException exception)
        {
            return "redirect:/employee/breeds";
        }
        BreedData breedData = new BreedData();
        breedData.setBreedId(id);
        breedData.setBreed(breed.getBreed());
        model.addAttribute("breedData", breedData);
        return "/employee/breeds/edit";
    }

    // POST: Edit breed
    @RequestMapping(value = "/employee/breeds/edit", method = RequestMethod.POST)
    public String postEditBreed(BreedData breedData, Model model)
    {
        if (CustomValidator.breedValidation(model, breedData.getBreed()))
        {
            model.addAttribute("breedData", breedData);
            return "/employee/breeds/edit";
        }
        Breeds breed = null;
        try {
            breed = breedsRepository.findByBreedId(Long.parseLong(breedData.getBreedId()));
            if(breed == null) throw new NumberFormatException("Brak");
        }
        catch (NumberFormatException exception)
        {
            return "redirect:/employee/breeds";
        }
        if(!breed.getBreed().equals(breedData.getBreed()) && !breedsRepository.findAllByBreed(breedData.getBreed()).isEmpty())
        {
            model.addAttribute("breedError", "Taka rasa już została dodana.");
            return "/employee/breeds/edit";
        }
        breed.setBreed(breedData.getBreed());
        breedsRepository.save(breed);
        return "redirect:/employee/breeds";
    }

    // GET: Breed details
    @RequestMapping(value = "/employee/breeds/{id}", method = RequestMethod.GET)
    public String getBreedDetails(@PathVariable(name = "id", value = "id") String id, Model model)
    {
        try {
            Breeds breed = breedsRepository.findByBreedId(Long.parseLong(id));
            model.addAttribute("breed", breed);
            if(breed == null) throw new NumberFormatException("Brak");
        }
        catch (NumberFormatException exception)
        {
            return "redirect:/employee/breeds";
        }
        return "/employee/breeds/details";
    }

    // GET: Species
    @RequestMapping(value = "/employee/species", method = RequestMethod.GET)
    public String getSpecies(Model model)
    {
        model.addAttribute("species", speciesRepository.findAll());
        return "/employee/species/index";
    }

    // GET: Search Species
    @RequestMapping(value = "/employee/species/search", method = RequestMethod.GET)
    public String getSearchSpecies(Model model)
    {
        model.addAttribute("searchData", new SearchData());
        return "/employee/species/search";
    }

    // POST: Search Species
    @RequestMapping(value = "/employee/species/search", method = RequestMethod.POST)
    public String postSearchSpecies(Model model, SearchData searchData)
    {
        model.addAttribute("searchData", searchData);
        String search = "";
        if(searchData.getValue().isEmpty())
            search = "/all/all";
        else
            search = "/" + searchData.getOption() + "/" + searchData.getValue();
        return "redirect:/employee/species/search" + search;
    }

    // GET: SearchScore Species
    @RequestMapping(value = "/employee/species/search/{option}/{value}", method = RequestMethod.GET)
    public String getSearchScoreSpecies(@PathVariable(name = "option", value = "option") String option, @PathVariable(name = "value", value = "value") String value, Model model)
    {
        List<Species> species = new ArrayList<Species>();
        if(option.equals("all"))
        {
            species = speciesRepository.findAll();
        }
        else if(option.equals("1"))
        {
            try {
                long id = Long.parseLong(value);
                species = speciesRepository.findAllBySpeciesId(id);
            }
            catch (Exception ignored) {}
        }
        else if(option.equals("2"))
        {
            species = speciesRepository.findAllBySpeciesContaining(value);
        }

        model.addAttribute("species", species);
        return "/employee/species/index";
    }

    // GET: Add species
    @RequestMapping(value = "/employee/species/add", method = RequestMethod.GET)
    public String getAddSpecies(Model model)
    {
        model.addAttribute("speciesData", new SpeciesData());
        return "/employee/species/add";
    }

    // POST: Add species
    @RequestMapping(value = "/employee/species/add", method = RequestMethod.POST)
    public String postAddSpecies(SpeciesData speciesData, Model model)
    {
        if (CustomValidator.speciesValidation(model, speciesData.getSpecies()))
        {
            model.addAttribute("speciesData", speciesData);
            return "/employee/species/add";
        }
        if(!speciesRepository.findAllBySpecies(speciesData.getSpecies()).isEmpty())
        {
            model.addAttribute("speciesError", "Taki gatunek już został dodany.");
            return "/employee/species/add";
        }
        Species species = new Species();
        species.setSpecies(speciesData.getSpecies());
        speciesRepository.save(species);
        return "redirect:/employee/species";
    }

    // GET: Edit species
    @RequestMapping(value = "/employee/species/edit/{id}", method = RequestMethod.GET)
    public String getEditSpecies(@PathVariable(name = "id", value = "id") String id, Model model)
    {
        Species species = null;
        try {
            species = speciesRepository.findBySpeciesId(Long.parseLong(id));
            if(species == null) throw new NumberFormatException("Brak");
        }
        catch (NumberFormatException exception)
        {
            return "redirect:/employee/species";
        }
        SpeciesData speciesData = new SpeciesData();
        speciesData.setSpeciesId(id);
        speciesData.setSpecies(species.getSpecies());
        model.addAttribute("speciesData", speciesData);
        return "/employee/species/edit";
    }

    // POST: Edit species
    @RequestMapping(value = "/employee/species/edit", method = RequestMethod.POST)
    public String postEditSpecies(SpeciesData speciesData, Model model)
    {
        if (CustomValidator.speciesValidation(model, speciesData.getSpecies()))
        {
            model.addAttribute("speciesData", speciesData);
            return "/employee/species/edit";
        }
        Species species = null;
        try {
            species = speciesRepository.findBySpeciesId(Long.parseLong(speciesData.getSpeciesId()));
            if(species == null) throw new NumberFormatException("Brak");
        }
        catch (NumberFormatException exception)
        {
            return "redirect:/employee/species";
        }
        if(!species.getSpecies().equals(speciesData.getSpecies()) && !speciesRepository.findAllBySpecies(speciesData.getSpecies()).isEmpty())
        {
            model.addAttribute("speciesError", "Taki gatunek już został dodany.");
            return "/employee/species/edit";
        }
        species.setSpecies(speciesData.getSpecies());
        speciesRepository.save(species);
        return "redirect:/employee/species";
    }

    // GET: Species details
    @RequestMapping(value = "/employee/species/{id}", method = RequestMethod.GET)
    public String getSpeciesDetails(@PathVariable(name = "id", value = "id") String id, Model model)
    {
        try {
            Species species = speciesRepository.findBySpeciesId(Long.parseLong(id));
            model.addAttribute("species", species);
            if(species == null) throw new NumberFormatException("Brak");
        }
        catch (NumberFormatException exception)
        {
            return "redirect:/employee/species";
        }
        return "/employee/species/details";
    }

    // GET: Visits
    @RequestMapping(value = "/employee/visits", method = RequestMethod.GET)
    public String getVisits(Model model)
    {
        model.addAttribute("visits", visitsRepository.findAll());
        return "/employee/visits/index";
    }

    // GET: Search Visits
    @RequestMapping(value = "/employee/visits/search", method = RequestMethod.GET)
    public String getSearchVisits(Model model)
    {
        model.addAttribute("searchData", new SearchData());
        return "/employee/visits/search";
    }

    // POST: Search Visits
    @RequestMapping(value = "/employee/visits/search", method = RequestMethod.POST)
    public String postSearchVisits(Model model, SearchData searchData)
    {
        model.addAttribute("searchData", searchData);
        String search = "";
        if(searchData.getValue().isEmpty())
            search = "/all/all";
        else
            search = "/" + searchData.getOption() + "/" + searchData.getValue();
        return "redirect:/employee/visits/search" + search;
    }

    // GET: SearchScore Visits
    @RequestMapping(value = "/employee/visits/search/{option}/{value}", method = RequestMethod.GET)
    public String getSearchScoreVisits(@PathVariable(name = "option", value = "option") String option, @PathVariable(name = "value", value = "value") String value, Model model)
    {
        List<Visits> visits = new ArrayList<Visits>();
        if(option.equals("all"))
        {
            visits = visitsRepository.findAll();
        }
        else if(option.equals("1"))
        {
            try {
                long id = Long.parseLong(value);
                visits = visitsRepository.findAllByVisitId(id);
            }
            catch (Exception ignored) {}
        }
        else if(option.equals("2"))
        {
            try {
                visits = visitsRepository.findAllByVisitDate(new SimpleDateFormat("yyyy-M-dd").parse(value));
            }
            catch (Exception ignored) {}
        }
        else if(option.equals("3"))
        {
            try {
                visits = visitsRepository.findAllByVisitDateBefore(new SimpleDateFormat("yyyy-M-dd").parse(value));
            }
            catch (Exception ignored) {}
        }
        else if(option.equals("4"))
        {
            try {
                visits = visitsRepository.findAllByVisitDateAfter(new SimpleDateFormat("yyyy-M-dd").parse(value));
            }
            catch (Exception ignored) {}
        }
        else if(option.equals("5"))
        {
            try {
                visits = visitsRepository.findAllByVisitPrice(Double.parseDouble(value));
            }
            catch (Exception ignored) {}
        }
        else if(option.equals("6"))
        {
            try {
                visits = visitsRepository.findAllByVisitPriceLessThan(Double.parseDouble(value));
            }
            catch (Exception ignored) {}
        }
        else if(option.equals("7"))
        {
            try {
                visits = visitsRepository.findAllByVisitPriceGreaterThan(Double.parseDouble(value));
            }
            catch (Exception ignored) {}
        }

        else if(option.equals("8"))
        {
            visits = visitsRepository.findAllByIsPaid(Boolean.parseBoolean(value));
        }
        else if(option.equals("9"))
        {
            visits = visitsRepository.findAllByVisitStatusContaining(value);
        }

        model.addAttribute("visits", visits);
        return "/employee/visits/index";
    }


    // GET: Add visit
    @RequestMapping(value = "/employee/visits/add/{id}", method = RequestMethod.GET)
    public String getAddVisit(@PathVariable(name = "id", value = "id") String id, Model model)
    {
        VisitData visitData = new VisitData();
        Pets pet = null;
        try
        {
            pet = petsRepository.findByPetId(Long.parseLong(id));
            if(pet == null) throw new NumberFormatException("Brak");
        }
        catch (NumberFormatException | EntityNotFoundException exception)
        {
            return "redirect:/employee/visits";
        }
        visitData.setPetId(id);
        model.addAttribute("visitData", visitData);
        return "/employee/visits/add";
    }

    // POST: Add visit
    @RequestMapping(value = "/employee/visits/add", method = RequestMethod.POST)
    public String postAddVisit(VisitData visitData, Model model) throws ParseException {
        if (CustomValidator.visitDateValidation(model, visitData.getVisitDate())
                || CustomValidator.visitTimeValidation(model, visitData.getVisitTime())
                || CustomValidator.visitPriceValidation(model, visitData.getVisitPrice())
                || CustomValidator.visitStatusValidation(model, visitData.getVisitStatus()))
        {
            model.addAttribute("visitData", visitData);
            return "/employee/visits/add";
        }
        Visits visit = new Visits();
        Pets pet = null;
        try
        {
            pet = petsRepository.findByPetId(Long.parseLong(visitData.getPetId()));
            if(pet == null) throw new NumberFormatException("Brak");
        }
        catch (NumberFormatException | EntityNotFoundException exception)
        {
            return "redirect:/employee/visits";
        }
        visit.setPetId(pet);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-dd HH:mm");
        visit.setVisitDate(simpleDateFormat.parse(visitData.getVisitDate() + " " + visitData.getVisitTime()));
        visit.setIsPaid(Boolean.valueOf(visitData.getIsPaid()));
        visit.setPrescriptions(null);
        visit.setVisitPrice(Double.parseDouble(visitData.getVisitPrice()));
        visit.setTreatmentsVisits(null);
        visit.setVisitStatus(visitData.getVisitStatus());
        visit.setPetId(pet);
        visitsRepository.save(visit);
        return "redirect:/employee/visits";
    }

    // GET: Add treatment to visit
    @RequestMapping(value = "/employee/visits/addTreatment/{id}", method = RequestMethod.GET)
    public String getAddTreatment2Visit(@PathVariable(name = "id", value = "id") String id, Model model)
    {
        try {
            if (!visitsRepository.existsById(Long.parseLong(id)))
                return "redirect:/employee/visits";
        }
        catch (NumberFormatException exception)
        {
            return "redirect:/employee/visits";
        }
        TreatmentVisitData treatmentVisitData = new TreatmentVisitData();
        treatmentVisitData.setVisitId(id);
        model.addAttribute("treatmentVisitData", treatmentVisitData);
        model.addAttribute("treatments", treatmentsRepository.findAll());
        return "/employee/visits/addTreatment";
    }

    // POST: Add treatment to visit
    @RequestMapping(value = "/employee/visits/addTreatment", method = RequestMethod.POST)
    public String postAddTreatment2Visit(TreatmentVisitData treatmentVisitData, Model model)
    {
        try {
            if (!visitsRepository.existsById(Long.parseLong(treatmentVisitData.getVisitId())))
                return "redirect:/employee/visits";
            if (!treatmentsRepository.existsById(Long.parseLong(treatmentVisitData.getTreatmentId())))
                return "redirect:/employee/visits";
        }
        catch (NumberFormatException exception)
        {
            return "redirect:/employee/visits";
        }
        TreatmentsVisits treatmentsVisits = new TreatmentsVisits();
        treatmentsVisits.setTreatmentId(treatmentsRepository.findByTreatmentId(Long.parseLong(treatmentVisitData.getTreatmentId())));
        treatmentsVisits.setVisitId(visitsRepository.findByVisitId(Long.parseLong(treatmentVisitData.getVisitId())));
        treatmentsVisitsRepository.save(treatmentsVisits);
        Treatments treatment = treatmentsRepository.findByTreatmentId(Long.parseLong(treatmentVisitData.getTreatmentId()));
        if (treatment.getTreatmentsVisits() == null)
            treatment.setTreatmentsVisits(new HashSet<>());
        treatment.getTreatmentsVisits().add(treatmentsVisits);
        treatmentsRepository.save(treatment);
        Visits visit = visitsRepository.findByVisitId(Long.parseLong(treatmentVisitData.getVisitId()));
        if (visit.getTreatmentsVisits() == null)
            visit.setTreatmentsVisits(new HashSet<>());
        visit.getTreatmentsVisits().add(treatmentsVisits);
        visitsRepository.save(visit);
        return "redirect:/employee/visits";
    }

    // GET: Edit visit
    @RequestMapping(value = "/employee/visits/edit/{id}", method = RequestMethod.GET)
    public String getEditVisit(@PathVariable(name = "id", value = "id") String id, Model model)
    {
        Visits visit = null;
        try
        {
            visit = visitsRepository.findByVisitId(Long.parseLong(id));
            if(visit == null) throw new NumberFormatException("Brak");
        }
        catch (NumberFormatException | EntityNotFoundException exception)
        {
            return "redirect:/employee/visits";
        }
        VisitData visitData = new VisitData();
        visitData.setPetId(visit.getPetId().getPetId().toString());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        StringTokenizer stringTokenizer = new StringTokenizer(simpleDateFormat.format(visit.getVisitDate()), " ");
        visitData.setVisitDate(stringTokenizer.nextToken());
        visitData.setVisitTime(stringTokenizer.nextToken());
        visitData.setVisitPrice(visit.getVisitPrice().toString());
        visitData.setVisitStatus(visit.getVisitStatus());
        visitData.setIsPaid(visit.getIsPaid().toString());
        visitData.setVisitId(id);
        model.addAttribute("visitData", visitData);
        model.addAttribute("pet", visit.getPetId());
        return "/employee/visits/edit";
    }

    // POST: Edit visit
    @RequestMapping(value = "/employee/visits/edit", method = RequestMethod.POST)
    public String postEditVisit(VisitData visitData, Model model) throws ParseException {
        if (CustomValidator.visitDateValidation(model, visitData.getVisitDate())
                || CustomValidator.visitTimeValidation(model, visitData.getVisitTime())
                || CustomValidator.visitPriceValidation(model, visitData.getVisitPrice())
                || CustomValidator.visitStatusValidation(model, visitData.getVisitStatus()))
        {
            model.addAttribute("visitData", visitData);
            return "/employee/visits/edit";
        }
        Visits visit = null;
        Pets pet = null;
        try
        {
            visit = visitsRepository.findByVisitId(Long.parseLong(visitData.getVisitId()));
            pet = petsRepository.findByPetId(Long.parseLong(visitData.getPetId()));
            if(visit == null || pet == null) throw new NumberFormatException("Brak");
        }
        catch (NumberFormatException | EntityNotFoundException exception)
        {
            return "redirect:/employee/visits";
        }
        visit.setPetId(pet);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-dd HH:mm");
        visit.setVisitDate(simpleDateFormat.parse(visitData.getVisitDate() + " " + visitData.getVisitTime()));
        visit.setVisitPrice(Double.parseDouble(visitData.getVisitPrice()));
        visit.setVisitStatus(visitData.getVisitStatus());
        visit.setIsPaid(Boolean.valueOf(visitData.getIsPaid()));
        visitsRepository.save(visit);
        return "redirect:/employee/visits";
    }

    // GET: Visit details
    @RequestMapping(value = "/employee/visits/{id}", method = RequestMethod.GET)
    public String getVisitDetails(@PathVariable(name = "id", value = "id") String id, Model model)
    {
        Visits visit = null;
        try
        {
            visit = visitsRepository.findByVisitId(Long.parseLong(id));
            if(visit == null) throw new NumberFormatException("Brak");
        }
        catch (NumberFormatException | EntityNotFoundException exception)
        {
            return "redirect:/employee/visits";
        }
        model.addAttribute("visit", visit);
        return "/employee/visits/details";
    }

    // GET: Clients
    @RequestMapping(value = "/employee/clients", method = RequestMethod.GET)
    public String getClients(Model model)
    {
        model.addAttribute("clients", personsRepository.findAllByIsStandardAccount(true));
        return "/employee/clients/index";
    }

    // GET: Search Clients
    @RequestMapping(value = "/employee/clients/search", method = RequestMethod.GET)
    public String getSearchClients(Model model)
    {
        model.addAttribute("searchData", new SearchData());
        return "/employee/clients/search";
    }

    // POST: Search Clients
    @RequestMapping(value = "/employee/clients/search", method = RequestMethod.POST)
    public String postSearchClients(Model model, SearchData searchData)
    {
        model.addAttribute("searchData", searchData);
        String search = "";
        if(searchData.getValue().isEmpty())
            search = "/all/all";
        else
            search = "/" + searchData.getOption() + "/" + searchData.getValue();
        return "redirect:/employee/clients/search" + search;
    }

    // GET: SearchScore Clients
    @RequestMapping(value = "/employee/clients/search/{option}/{value}", method = RequestMethod.GET)
    public String getSearchScoreClients(@PathVariable(name = "option", value = "option") String option, @PathVariable(name = "value", value = "value") String value, Model model)
    {
        List<Persons> persons = new ArrayList<Persons>();
        if(option.equals("all"))
        {
            persons = personsRepository.findAllByIsStandardAccount(true);
        }
        else if(option.equals("1"))
        {
            try {
                long id = Long.parseLong(value);
                persons = personsRepository.findAllByIsStandardAccountAndPersonId(true, id);
            }
            catch (Exception ignored) {}
        }
        else if(option.equals("2"))
        {
            try {
                List<Accounts> accounts = accountsRepository.findAllByEmailContaining(value);
                persons = personsRepository.findAllByIsStandardAccountAndAccountIdIn(true, accounts);
            }
            catch (Exception ignored) {}
        }
        else if(option.equals("3"))
        {
            persons = personsRepository.findAllByIsStandardAccountAndNameContaining(true, value);
        }
        else if(option.equals("4"))
        {
            persons = personsRepository.findAllByIsStandardAccountAndSurnameContaining(true, value);
        }
        else if(option.equals("5"))
        {
            persons = personsRepository.findAllByIsStandardAccountAndPeselContaining(true, value);
        }
        else if(option.equals("6"))
        {
            persons = personsRepository.findAllByIsStandardAccountAndTelephoneContaining(true, value);
        }
        else if(option.equals("7"))
        {
            List<Cities> cities = citiesRepository.findAllByPostcodeContaining(value);
            persons = personsRepository.findAllByIsStandardAccountAndCityIdIn(true, cities);
        }
        else if(option.equals("8"))
        {
            List<Cities> cities = citiesRepository.findAllByCityContaining(value);
            persons = personsRepository.findAllByIsStandardAccountAndCityIdIn(true, cities);
        }
        else if(option.equals("9"))
        {
            persons = personsRepository.findAllByIsStandardAccountAndAddressContaining(true, value);
        }

        model.addAttribute("clients", persons);
        return "/employee/clients/index";
    }

    // GET: Add client
    @RequestMapping(value = "/employee/clients/add", method = RequestMethod.GET)
    public String getAddClient(Model model)
    {
        RegisterData registerData = new RegisterData();
        model.addAttribute("registerData", registerData);
        return "/employee/clients/add";
    }

    // POST: Add client
    @RequestMapping(value = "/employee/clients/add", method = RequestMethod.POST)
    public String postAddClient(RegisterData registerData, Model model)
    {
        if (CustomValidator.cityValidation(model, registerData.getCity())
                || CustomValidator.nameValidation(model, registerData.getName())
                || CustomValidator.peselValidation(model, registerData.getPesel())
                || CustomValidator.phoneNumberValidation(model, registerData.getTelephone())
                || CustomValidator.streetValidation(model, registerData.getAddress())
                || CustomValidator.postcodeValidation(model, registerData.getPostCode()))
        {
            model.addAttribute("registerData", registerData);
            return "/employee/clients/add";
        }
        if (!registerData.getEmail().isBlank() || !registerData.getEmail().isEmpty())
        {
            if (CustomValidator.passwordValidation(model, registerData.getPassword1())
                    || CustomValidator.passwordConfirmValidation(model, registerData.getPassword2(), registerData.getPassword1())
                    || CustomValidator.emailValidation(model, registerData.getEmail()))
            {
                model.addAttribute("registerData", registerData);
                return "/employee/clients/add";
            }
            if(!accountsRepository.findAllByEmail(registerData.getEmail()).isEmpty())
            {
                model.addAttribute("emailError", "Podany adres e-mail jest już zajęty.");
                model.addAttribute("register", registerData);
                return "/employee/clients/add";
            }
        }
        Accounts account = new Accounts();
        if (!registerData.getEmail().isBlank() || !registerData.getEmail().isEmpty())
        {
            account.setEmail(registerData.getEmail());
            account.setPassword(new BCryptPasswordEncoder().encode(registerData.getPassword1()));
            account.setEnabled(true);
            account.setExpired(false);
            account.setLocked(false);
            accountsRepository.save(account);
            Roles role = rolesRepository.findByRole("ROLE_USER");
            AccountsRoles accountsRole = new AccountsRoles();
            accountsRole.setRoleId(role);
            accountsRole.setAccountId(account);
            accountsRolesRepository.save(accountsRole);
            role.getAccountsRoles().add(accountsRole);
            rolesRepository.save(role);
            account.getAccountsRoles().add(accountsRole);
            accountsRepository.save(account);
        }
        Persons person = new Persons();
        person.setName(registerData.getName());
        person.setPesel(registerData.getPesel());
        person.setAddress(registerData.getAddress());
        person.setSurname(registerData.getSurname());
        person.setTelephone(registerData.getTelephone());
        if (citiesRepository.countByPostcode(registerData.getPostCode()) == 0 && citiesRepository.countByCity(registerData.getCity()) == 0)
        {
            Cities city = new Cities();
            city.setPostcode(registerData.getPostCode());
            city.setCity(registerData.getCity());
            citiesRepository.save(city);
            person.setCityId(city);
            personsRepository.save(person);
            city.getPersons().add(person);
            citiesRepository.save(city);
        }
        else if (citiesRepository.countByPostcode(registerData.getPostCode()) != 0)
        {
            List<Cities> cities = citiesRepository.findAllByPostcode(registerData.getPostCode());
            person.setCityId(cities.get(0));
            personsRepository.save(person);
            cities.get(0).getPersons().add(person);
            citiesRepository.save(cities.get(0));
        }
        else if (citiesRepository.countByCity(registerData.getCity()) != 0)
        {
            List<Cities> cities = citiesRepository.findAllByCity(registerData.getCity());
            person.setCityId(cities.get(0));
            personsRepository.save(person);
            cities.get(0).getPersons().add(person);
            citiesRepository.save(cities.get(0));
        }
        else
        {
            List<Cities> cities = citiesRepository.findAllByPostcodeAndCity(registerData.getPostCode(), registerData.getCity());
            person.setCityId(cities.get(0));
            personsRepository.save(person);
            cities.get(0).getPersons().add(person);
            citiesRepository.save(cities.get(0));
        }
        if (!registerData.getEmail().isBlank() || !registerData.getEmail().isEmpty())
        {
            person.setAccountId(account);
            account.setPersonId(person);
            personsRepository.save(person);
            accountsRepository.save(account);
        }
        else
        {
            person.setAccountId(null);
            personsRepository.save(person);
        }
        return "redirect:/employee/clients";
    }

    // GET: Edit client
    @RequestMapping(value = "/employee/clients/edit/{id}", method = RequestMethod.GET)
    public String getEditClient(@PathVariable(name = "id", value = "id") String id, Model model)
    {
        PersonData personData = new PersonData();
        Persons person = null;
        try {
            person = personsRepository.findByPersonId(Long.parseLong(id));
            if(person == null) throw new NumberFormatException("Brak");
        }
        catch (NumberFormatException exception)
        {
            return "redirect:/employee/clients";
        }

        personData.setPersonId(id);
        personData.setAddress(person.getAddress());
        personData.setPesel(person.getPesel());
        personData.setCity(person.getCityId().getCity());
        personData.setName(person.getName());
        personData.setSurname(person.getSurname());
        personData.setTelephone(person.getTelephone());
        personData.setIsStandardAccount(person.getIsStandardAccount().toString());
        personData.setPostCode(person.getCityId().getPostcode());
        model.addAttribute("personData", personData);
        return "/employee/clients/edit";
    }

    // POST: Edit client
    @RequestMapping(value = "/employee/clients/edit", method = RequestMethod.POST)
    public String postEditClient(PersonData personData, Model model)
    {
        if (CustomValidator.nameValidation(model, personData.getName())
                || CustomValidator.surnameValidation(model, personData.getSurname())
                || CustomValidator.peselValidation(model, personData.getPesel())
                || CustomValidator.phoneNumberValidation(model, personData.getTelephone())
                || CustomValidator.postcodeValidation(model, personData.getPostCode())
                || CustomValidator.cityValidation(model, personData.getCity())
                || CustomValidator.streetValidation(model, personData.getAddress()))
        {
            model.addAttribute("register", personData);
            return "/employee/clients/edit";
        }
        Persons person = null;
        try {
            person = personsRepository.findByPersonId(Long.parseLong(personData.getPersonId()));
            if(person == null) throw new NumberFormatException("Brak");
        }
        catch (NumberFormatException exception)
        {
            return "redirect:/employee/clients";
        }
        person.setAddress(personData.getAddress());
        person.setPesel(personData.getPesel());
        person.setName(personData.getName());
        person.setSurname(personData.getSurname());
        person.setTelephone(personData.getTelephone());
        person.setIsStandardAccount(Boolean.valueOf(personData.getIsStandardAccount()));
        if (citiesRepository.countByPostcode(personData.getPostCode()) == 0 && citiesRepository.countByCity(personData.getCity()) == 0)
        {
            Cities city = new Cities();
            city.setPostcode(personData.getPostCode());
            city.setCity(personData.getCity());
            citiesRepository.save(city);
            person.setCityId(city);
            personsRepository.save(person);
            city.getPersons().add(person);
            citiesRepository.save(city);
        }
        else if (citiesRepository.countByPostcode(personData.getPostCode()) != 0)
        {
            List<Cities> cities = citiesRepository.findAllByPostcode(personData.getPostCode());
            person.setCityId(cities.get(0));
            personsRepository.save(person);
            cities.get(0).getPersons().add(person);
            citiesRepository.save(cities.get(0));
        }
        else if (citiesRepository.countByCity(personData.getCity()) != 0)
        {
            List<Cities> cities = citiesRepository.findAllByCity(personData.getCity());
            person.setCityId(cities.get(0));
            personsRepository.save(person);
            cities.get(0).getPersons().add(person);
            citiesRepository.save(cities.get(0));
        }
        else
        {
            List<Cities> cities = citiesRepository.findAllByPostcodeAndCity(personData.getPostCode(), personData.getCity());
            person.setCityId(cities.get(0));
            personsRepository.save(person);
            cities.get(0).getPersons().add(person);
            citiesRepository.save(cities.get(0));
        }
        return "redirect:/employee/clients";
    }

    // GET: Client details
    @RequestMapping(value = "/employee/clients/{id}", method = RequestMethod.GET)
    public String getClientDetails(@PathVariable(name = "id", value = "id") String id, Model model)
    {
        try {
            Persons person = personsRepository.findByPersonId(Long.parseLong(id));
            if(person == null) throw new NumberFormatException("Brak");
            model.addAttribute("client", person);
        }
        catch (NumberFormatException exception)
        {
            return "redirect:/employee/clients";
        }
        return "/employee/clients/details";
    }

    // GET: Client pets
    @RequestMapping(value = "/employee/clients/pets/{id}", method = RequestMethod.GET)
    public String getClientPets(@PathVariable(name = "id", value = "id") String id, Model model)
    {
        try {
            Persons person = personsRepository.findByPersonId(Long.parseLong(id));
            if(person == null) throw new NumberFormatException("Brak");
            model.addAttribute("pets", petsRepository.findAllByPersonId(person));
        }
        catch (NumberFormatException exception)
        {
            return "redirect:/employee/clients";
        }
        return "/employee/pets/index";
    }

    // GET: Client Add pet
    @RequestMapping(value = "/employee/clients/addPet/{id}", method = RequestMethod.GET)
    public String getClientAddPet(@PathVariable(name = "id", value = "id") String id, Model model)
    {
        List<Species> species = speciesRepository.findAll();
        List<Breeds> breeds = breedsRepository.findAll();
        List<Persons> persons = personsRepository.findAll();
        model.addAttribute("breeds", breeds);
        model.addAttribute("species", species);
        model.addAttribute("persons", persons);
        try {
            Persons person = personsRepository.findByPersonId(Long.parseLong(id));
            if(person == null) throw new NumberFormatException("Brak");
            PetData petData = new PetData();
            petData.setPersonId(person.getPersonId().toString());
            model.addAttribute("petData", petData);
        }
        catch (NumberFormatException exception)
        {
            return "redirect:/employee/clients";
        }
        return "/employee/pets/add";
    }

    // GET: Add client account
    @RequestMapping(value = "/employee/clients/add/account/{id}", method = RequestMethod.GET)
    public String getAddAccount(@PathVariable(name = "id", value = "id") String id, Model model) {
        try {
            if (!personsRepository.existsById(Long.parseLong(id)))
                return "redirect:/employee/clients";
            if (personsRepository.findAllByPersonId(Long.parseLong(id)).get(0).getAccountId() != null)
                return "redirect:/employee/clients";
        }
        catch (NumberFormatException exception)
        {
            return "redirect:/employee/clients";
        }
        ClientAccountData clientAccountData = new ClientAccountData();
        clientAccountData.setPersonId(id);
        model.addAttribute("clientAccountData", clientAccountData);
        return "/employee/clients/add_account";
    }

    // POST: Add client account
    @RequestMapping(value = "/employee/clients/add/account/", method = RequestMethod.POST)
    public String postAddAccount(ClientAccountData clientAccountData, Model model)
    {
        if (CustomValidator.passwordValidation(model, clientAccountData.getPassword1())
                || CustomValidator.passwordConfirmValidation(model, clientAccountData.getPassword2(), clientAccountData.getPassword1())
                || CustomValidator.emailValidation(model, clientAccountData.getEmail()))
        {
            model.addAttribute("clientAccountData", clientAccountData);
            return "/employee/clients/add_account";
        }
        Persons person = personsRepository.findByPersonId(Long.parseLong(clientAccountData.getPersonId()));
        if(person == null) throw new NumberFormatException("Brak");
        try {
            if (!personsRepository.existsById(person.getPersonId()))
                return "redirect:/employee/clients";
            if (personsRepository.findAllByPersonId(person.getPersonId()).get(0).getAccountId() != null)
                return "redirect:/employee/clients";
        }
        catch (NumberFormatException exception)
        {
            return "redirect:/employee/clients";
        }
        if(!accountsRepository.findAllByEmail(clientAccountData.getEmail()).isEmpty())
        {
            model.addAttribute("emailError", "Podany adres e-mail jest już zajęty.");
            model.addAttribute("clientAccountData", clientAccountData);
            return "/employee/clients/add_account";
        }
        Accounts account = new Accounts();
        account.setEmail(clientAccountData.getEmail());
        account.setPassword(new BCryptPasswordEncoder().encode(clientAccountData.getPassword1()));
        account.setEnabled(true);
        account.setExpired(false);
        account.setLocked(false);
        accountsRepository.save(account);
        Roles role = rolesRepository.findByRole("ROLE_USER");
        AccountsRoles accountsRole = new AccountsRoles();
        accountsRole.setRoleId(role);
        accountsRole.setAccountId(account);
        accountsRolesRepository.save(accountsRole);
        role.getAccountsRoles().add(accountsRole);
        rolesRepository.save(role);
        account.getAccountsRoles().add(accountsRole);
        accountsRepository.save(account);
        person.setAccountId(account);
        personsRepository.save(person);
        account.setPersonId(person);
        accountsRepository.save(account);
        return "redirect:/employee/clients";
    }

    // GET: Prescriptions
    @RequestMapping(value = "/employee/prescriptions", method = RequestMethod.GET)
    public String getPrescriptions(Model model)
    {
        model.addAttribute("prescriptions", prescriptionsRepository.findAll());
        return "/employee/prescriptions/index";
    }

    // GET: Search Prescriptions
    @RequestMapping(value = "/employee/prescriptions/search", method = RequestMethod.GET)
    public String getSearchPrescriptions(Model model)
    {
        model.addAttribute("searchData", new SearchData());
        return "/employee/prescriptions/search";
    }

    // POST: Search Prescriptions
    @RequestMapping(value = "/employee/prescriptions/search", method = RequestMethod.POST)
    public String postSearchPrescriptions(Model model, SearchData searchData)
    {
        model.addAttribute("searchData", searchData);
        String search = "";
        if(searchData.getValue().isEmpty())
            search = "/all/all";
        else
            search = "/" + searchData.getOption() + "/" + searchData.getValue();
        return "redirect:/employee/prescriptions/search" + search;
    }

    // GET: SearchScore Prescriptions
    @RequestMapping(value = "/employee/prescriptions/search/{option}/{value}", method = RequestMethod.GET)
    public String getSearchScorePrescriptions(@PathVariable(name = "option", value = "option") String option, @PathVariable(name = "value", value = "value") String value, Model model)
    {
        List<Prescriptions> prescriptions = new ArrayList<Prescriptions>();
        if(option.equals("all"))
        {
            prescriptions = prescriptionsRepository.findAll();
        }
        else if(option.equals("1"))
        {
            try {
                long id = Long.parseLong(value);
                prescriptions = prescriptionsRepository.findAllByPrescriptionId(id);
            }
            catch (Exception ignored) {}
        }
        else if(option.equals("2"))
        {
            try {
                long id = Long.parseLong(value);
                Visits visits = visitsRepository.findByVisitId(id);
                prescriptions = prescriptionsRepository.findAllByVisitId(visits);
            }
            catch (Exception ignored) {}
        }
        else if(option.equals("3"))
        {
            try {
                long id = Long.parseLong(value);
                Persons persons = personsRepository.findByIsStandardAccountAndPersonId(false, id);
                prescriptions = prescriptionsRepository.findAllByDoctorId(persons);
            }
            catch (Exception ignored) {}
        }
        else if(option.equals("5"))
        {
            try {
                long id = Long.parseLong(value);
                Persons persons = personsRepository.findByIsStandardAccountAndPersonId(true, id);
                prescriptions = prescriptionsRepository.findAllByClientId(persons);
            }
            catch (Exception ignored) {}
        }
        else if(option.equals("6"))
        {
            try {
                prescriptions = prescriptionsRepository.findAllByPrescriptionDate(new SimpleDateFormat("yyyy-M-dd").parse(value));
            }
            catch (Exception ignored) {}
        }
        else if(option.equals("7"))
        {
            try {
                prescriptions = prescriptionsRepository.findAllByPrescriptionDateBefore(new SimpleDateFormat("yyyy-M-dd").parse(value));
            }
            catch (Exception ignored) {}
        }
        else if(option.equals("8"))
        {
            try {
                prescriptions = prescriptionsRepository.findAllByPrescriptionDateAfter(new SimpleDateFormat("yyyy-M-dd").parse(value));
            }
            catch (Exception ignored) {}
        }

        model.addAttribute("prescriptions", prescriptions);
        return "/employee/prescriptions/index";
    }

    // GET: Add prescription
    @RequestMapping(value = "/employee/prescriptions/add/{id}", method = RequestMethod.GET)
    public String getAddPrescription(@PathVariable(name = "id", value = "id") String id, Model model)
    {
        Visits visit = null;
        try {
            visit = visitsRepository.findByVisitId(Long.parseLong(id));
            if (visit == null) throw new NumberFormatException("Brak");
        }
        catch (NumberFormatException | EntityNotFoundException exception)
        {
            return "redirect:/employee/visits";
        }
        PrescriptionData prescriptionData = new PrescriptionData();
        prescriptionData.setVisitId(id);
        model.addAttribute("prescriptionData", prescriptionData);
        model.addAttribute("clients", personsRepository.findAllByIsStandardAccount(true));
        return "/employee/prescriptions/add";
    }

    // POST: Add prescription
    @RequestMapping(value = "/employee/prescriptions/add", method = RequestMethod.POST)
    public String postAddPrescription(PrescriptionData prescriptionData, Model model) throws ParseException {
        if (CustomValidator.prescriptionDateValidation(model, prescriptionData.getPrescriptionDate()))
        {
            model.addAttribute("prescriptionData", prescriptionData);
            model.addAttribute("clients", personsRepository.findAllByIsStandardAccount(true));
            return "/employee/prescriptions/add";
        }
        Prescriptions prescription = new Prescriptions();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        prescription.setPrescriptionDate(simpleDateFormat.parse(prescriptionData.getPrescriptionDate()));
        Persons client = null;
        try {
            if(prescriptionData.getClientId().isEmpty())
                throw new NumberFormatException("Brak");
            client = personsRepository.findByPersonId(Long.parseLong(prescriptionData.getClientId()));
            if(client == null) throw new NumberFormatException("Brak");
        }
        catch (NumberFormatException | EntityNotFoundException exception)
        {
            return "redirect:/employee/prescriptions";
        }
        prescription.setClientId(client);
        AccountDetails user = (AccountDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Persons doctor = accountsRepository.findByEmailAndIsEnabled(user.getUsername(), true).getPersonId();
        prescription.setDoctorId(doctor);
        prescription.setVisitId(visitsRepository.findByVisitId(Long.parseLong(prescriptionData.getVisitId())));
        prescription.setMedsPrescriptions(null);
        prescriptionsRepository.save(prescription);
        return "redirect:/employee/prescriptions";
    }

    // GET: Add medicine to prescription
    @RequestMapping(value = "/employee/prescriptions/addMedicine/{id}", method = RequestMethod.GET)
    public String getAddMedicine2Prescription(@PathVariable(name = "id", value = "id") String id, Model model)
    {
        try {
            if (!prescriptionsRepository.existsById(Long.parseLong(id)))
                return "redirect:/employee/prescriptions";
        }
        catch (NumberFormatException exception)
        {
            return "redirect:/employee/prescriptions";
        }
        MedPrescriptionData medPrescriptionData = new MedPrescriptionData();
        medPrescriptionData.setPrescriptionId(id);
        medPrescriptionData.setMedQuantity("1");
        model.addAttribute("medPrescriptionData", medPrescriptionData);
        model.addAttribute("meds", medsRepository.findAll());
        return "/employee/prescriptions/addMedicine";
    }

    // POST: Add medicine to prescription
    @RequestMapping(value = "/employee/prescriptions/addMedicine", method = RequestMethod.POST)
    public String postAddMedicine2Prescription(MedPrescriptionData medPrescriptionData, Model model)
    {
        try {
            if (!prescriptionsRepository.existsById(Long.parseLong(medPrescriptionData.getPrescriptionId())))
                return "redirect:/employee/prescriptions";
            if (!medsRepository.existsById(Long.parseLong(medPrescriptionData.getMedicineId())))
                return "redirect:/employee/prescriptions";
        }
        catch (NumberFormatException exception)
        {
            return "redirect:/employee/prescriptions";
        }
        MedsPrescriptions medPrescription = new MedsPrescriptions();
        medPrescription.setPrescriptionId(prescriptionsRepository.findByPrescriptionId(Long.parseLong(medPrescriptionData.getPrescriptionId())));
        medPrescription.setMedicineId(medsRepository.findByMedicineId(Long.parseLong(medPrescriptionData.getMedicineId())));
        medPrescription.setPrescriptionDescription(medPrescriptionData.getPrescriptionDescription());
        medPrescription.setMedQuantity(Integer.parseInt(medPrescriptionData.getMedQuantity()));
        medsPrescriptionsRepository.save(medPrescription);
        Prescriptions prescription = prescriptionsRepository.findByPrescriptionId(Long.parseLong(medPrescriptionData.getPrescriptionId()));
        if (prescription.getMedsPrescriptions() == null)
            prescription.setMedsPrescriptions(new HashSet<>());
        prescription.getMedsPrescriptions().add(medPrescription);
        prescriptionsRepository.save(prescription);
        Meds med = medsRepository.findByMedicineId(Long.parseLong(medPrescriptionData.getMedicineId()));
        if (med.getMedsPrescriptions() == null)
            med.setMedsPrescriptions(new HashSet<>());
        med.getMedsPrescriptions().add(medPrescription);
        medsRepository.save(med);
        return "redirect:/employee/prescriptions";
    }

    // GET: Edit prescription
    @RequestMapping(value = "/employee/prescriptions/edit/{id}", method = RequestMethod.GET)
    public String getEditPrescription(@PathVariable(name = "id", value = "id") String id, Model model)
    {
        PrescriptionData prescriptionData = new PrescriptionData();
        Prescriptions prescription = null;
        try {
            prescription = prescriptionsRepository.findByPrescriptionId(Long.parseLong(id));
            if(prescription == null) throw new NumberFormatException("Brak");
        }
        catch (NumberFormatException | EntityNotFoundException exception)
        {
            return "redirect:/employee/prescriptions";
        }
        prescriptionData.setPrescriptionId(id);
        prescriptionData.setClientId(prescription.getClientId().getPersonId().toString());
        prescriptionData.setDoctorId(prescription.getDoctorId().getPersonId().toString());
        prescriptionData.setPrescriptionDate(prescription.getPrescriptionDate().toString());
        model.addAttribute("prescriptionData", prescriptionData);
        model.addAttribute("clients", personsRepository.findAllByIsStandardAccount(true));
        return "/employee/prescriptions/edit";
    }

    // POST: Edit prescription
    @RequestMapping(value = "/employee/prescriptions/edit", method = RequestMethod.POST)
    public String postEditPrescription(PrescriptionData prescriptionData, Model model) throws ParseException {
        if (CustomValidator.prescriptionDateValidation(model, prescriptionData.getPrescriptionDate()))
        {
            model.addAttribute("prescriptionData", prescriptionData);
            model.addAttribute("clients", personsRepository.findAllByIsStandardAccount(true));
            return "/employee/prescriptions/edit";
        }
        Prescriptions prescription = null;
        Persons client = null;
        try {
            prescription = prescriptionsRepository.findByPrescriptionId(Long.parseLong(prescriptionData.getPrescriptionId()));
            client = personsRepository.findAllByIsStandardAccountAndPersonId(true, Long.parseLong(prescriptionData.getClientId())).get(0);
            if(prescription == null || client == null) throw new NumberFormatException("Brak");
        }
        catch (NumberFormatException | EntityNotFoundException exception)
        {
            return "redirect:/employee/prescriptions";
        }
        prescription.setClientId(client);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        prescription.setPrescriptionDate(simpleDateFormat.parse(prescriptionData.getPrescriptionDate()));
        prescriptionsRepository.save(prescription);
        return "redirect:/employee/prescriptions";
    }

    // GET: Prescription details
    @RequestMapping(value = "/employee/prescriptions/{id}", method = RequestMethod.GET)
    public String getPrescriptionDetails(@PathVariable(name = "id", value = "id") String id, Model model)
    {
        try {
            Prescriptions prescription = null;
            prescription = prescriptionsRepository.findByPrescriptionId(Long.parseLong(id));
            if(prescription == null) throw new NumberFormatException("Brak");
            model.addAttribute("prescription", prescription);
        }
        catch (NumberFormatException | EntityNotFoundException exception)
        {
            return "redirect:/employee/prescriptions";
        }
        return "/employee/prescriptions/details";
    }
}
