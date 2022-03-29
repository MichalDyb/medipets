package kpu.krosno.MediPets.controllers;

import kpu.krosno.MediPets.models.*;
import kpu.krosno.MediPets.repositories.*;
import kpu.krosno.MediPets.utility.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ManagementController {
    @Autowired
    private AccountsRepository accountsRepository;
    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private AccountsRolesRepository accountsRolesRepository;
    @Autowired
    private CitiesRepository citiesRepository;
    @Autowired
    private PersonsRepository personsRepository;
    @Autowired
    private MedsRepository medsRepository;
    @Autowired
    private TreatmentsRepository treatmentsRepository;
    @Autowired
    private TreatmentsSpeciesRepository treatmentsSpeciesRepository;
    @Autowired
    private SpeciesRepository speciesRepository;

    // GET: Index
    @RequestMapping(value = "/management", method = RequestMethod.GET)
    public String getIndex(Model model)
    {
        return "/management/index";
    }

    // GET: Employees
    @RequestMapping(value = "/management/employees", method = RequestMethod.GET)
    public String getEmployees(Model model)
    {
        model.addAttribute("employees", personsRepository.findAllByIsStandardAccount(false));
        return "/management/employees/index";
    }

    // GET: Search Employees
    @RequestMapping(value = "/management/employees/search", method = RequestMethod.GET)
    public String getSearchEmployees(Model model)
    {
        model.addAttribute("searchData", new SearchData());
        return "/management/employees/search";
    }

    // POST: Search Employees
    @RequestMapping(value = "/management/employees/search", method = RequestMethod.POST)
    public String postSearchEmployees(Model model, SearchData searchData)
    {
        model.addAttribute("searchData", searchData);
        String search = "";
        if(searchData.getValue().isEmpty())
            search = "/all/all";
        else
            search = "/" + searchData.getOption() + "/" + searchData.getValue();
        return "redirect:/management/employees/search" + search;
    }

    // GET: SearchScore Employees
    @RequestMapping(value = "/management/employees/search/{option}/{value}", method = RequestMethod.GET)
    public String getSearchEmployees(@PathVariable(name = "option", value = "option") String option, @PathVariable(name = "value", value = "value") String value, Model model)
    {
        List<Persons> persons = new ArrayList<Persons>();
        if(option.equals("all"))
        {
            persons = personsRepository.findAllByIsStandardAccount(false);
        }
        else if(option.equals("1"))
        {
            try {
                long id = Long.parseLong(value);
                persons = personsRepository.findAllByIsStandardAccountAndPersonId(false, id);
            }
            catch (Exception ignored) {}
        }
        else if(option.equals("2"))
        {
            try {
                List<Accounts> accounts = accountsRepository.findAllByEmailContaining(value);
                persons = personsRepository.findAllByIsStandardAccountAndAccountIdIn(false, accounts);
            }
            catch (Exception ignored) {}
        }
        else if(option.equals("3"))
        {
            persons = personsRepository.findAllByIsStandardAccountAndNameContaining(false, value);
        }
        else if(option.equals("4"))
        {
            persons = personsRepository.findAllByIsStandardAccountAndSurnameContaining(false, value);
        }
        else if(option.equals("5"))
        {
            persons = personsRepository.findAllByIsStandardAccountAndPeselContaining(false, value);
        }
        else if(option.equals("6"))
        {
            persons = personsRepository.findAllByIsStandardAccountAndTelephoneContaining(false, value);
        }
        else if(option.equals("7"))
        {
            List<Cities> cities = citiesRepository.findAllByPostcodeContaining(value);
            persons = personsRepository.findAllByIsStandardAccountAndCityIdIn(false, cities);
        }
        else if(option.equals("8"))
        {
            List<Cities> cities = citiesRepository.findAllByCityContaining(value);
            persons = personsRepository.findAllByIsStandardAccountAndCityIdIn(false, cities);
        }
        else if(option.equals("9"))
        {
            persons = personsRepository.findAllByIsStandardAccountAndAddressContaining(false, value);
        }

        model.addAttribute("employees", persons);
        return "/management/employees/index";
    }

    // GET: Add Employee
    @RequestMapping(value = "/management/employees/add", method = RequestMethod.GET)
    public String getAddEmployee(Model model)
    {
        RegisterData registerData = new RegisterData();
        model.addAttribute("registerData", registerData);
        return "/management/employees/add";
    }

    // POST: Add Employee
    @RequestMapping(value = "/management/employees/add", method = RequestMethod.POST)
    public String postAddEmployee(RegisterData registerData, Model model)
    {
        if (CustomValidator.cityValidation(model, registerData.getCity())
                || CustomValidator.nameValidation(model, registerData.getName())
                || CustomValidator.peselValidation(model, registerData.getPesel())
                || CustomValidator.phoneNumberValidation(model, registerData.getTelephone())
                || CustomValidator.streetValidation(model, registerData.getAddress())
                || CustomValidator.postcodeValidation(model, registerData.getPostCode())
                || CustomValidator.passwordValidation(model, registerData.getPassword1())
                || CustomValidator.passwordConfirmValidation(model, registerData.getPassword2(), registerData.getPassword1())
                || CustomValidator.emailValidation(model, registerData.getEmail()))
        {
            model.addAttribute("registerData", registerData);
            return "/management/employees/add";
        }
        if(!accountsRepository.findAllByEmail(registerData.getEmail()).isEmpty())
        {
            model.addAttribute("emailError", "Podany adres e-mail jest już zajęty.");
            model.addAttribute("register", registerData);
            return "/management/employees/add";
        }
        Accounts account = new Accounts();
        account.setEmail(registerData.getEmail());
        account.setPassword(new BCryptPasswordEncoder().encode(registerData.getPassword1()));
        account.setEnabled(true);
        account.setExpired(false);
        account.setLocked(false);
        accountsRepository.save(account);
        Roles role = rolesRepository.findByRole("ROLE_STAFF");
        AccountsRoles accountsRole = new AccountsRoles();
        accountsRole.setRoleId(role);
        accountsRole.setAccountId(account);
        accountsRolesRepository.save(accountsRole);
        role.getAccountsRoles().add(accountsRole);
        rolesRepository.save(role);
        account.getAccountsRoles().add(accountsRole);
        accountsRepository.save(account);
        Persons person = new Persons();
        person.setName(registerData.getName());
        person.setAccountId(account);
        person.setPesel(registerData.getPesel());
        person.setAddress(registerData.getAddress());
        person.setSurname(registerData.getSurname());
        person.setTelephone(registerData.getTelephone());
        person.setIsStandardAccount(false);
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
        account.setPersonId(person);
        accountsRepository.save(account);
        return "redirect:/management/employees";
    }

    // GET: Edit Employee
    @RequestMapping(value = "/management/employees/edit/{id}", method = RequestMethod.GET)
    public String getEditEmployee(@PathVariable(name = "id", value = "id") String id, Model model)
    {
        PersonData personData = new PersonData();
        Persons person = null;
        try {
            person = personsRepository.findByPersonId(Long.parseLong(id));
            if(person == null) throw new NumberFormatException("Brak");
        }
        catch (NumberFormatException exception)
        {
            return "redirect:/management/employees";
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
        return "/management/employees/edit";
    }

    // POST: Edit Employee
    @RequestMapping(value = "/management/employees/edit", method = RequestMethod.POST)
    public String postEditEmployee(PersonData personData, Model model)
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
            return "/management/employees/edit";
        }
        Persons person = null;
        try {
            person = personsRepository.findByPersonId(Long.parseLong(personData.getPersonId()));
            if(person == null) throw new NumberFormatException("Brak");
        }
        catch (NumberFormatException exception)
        {
            return "redirect:/management/employees";
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
        return "redirect:/management/employees";
    }

    // GET: Employee Details
    @RequestMapping(value = "/management/employees/{id}", method = RequestMethod.GET)
    public String getEmployeeDetails(@PathVariable(name = "id", value = "id") String id, Model model)
    {
        try {
            Persons person = personsRepository.findByPersonId(Long.parseLong(id));
            model.addAttribute("employee", personsRepository.findByPersonId(Long.parseLong(id)));
            if(person == null) throw new NumberFormatException("Brak");
        }
        catch (NumberFormatException exception)
        {
            return "redirect:/management/employees";
        }
        return "/management/employees/details";
    }

    // GET: Meds
    @RequestMapping(value = "/management/meds", method = RequestMethod.GET)
    public String getMeds(Model model)
    {
        model.addAttribute("meds", medsRepository.findAll());
        return "/management/meds/index";
    }

    // GET: Search Meds
    @RequestMapping(value = "/management/meds/search", method = RequestMethod.GET)
    public String getSearchMeds(Model model)
    {
        model.addAttribute("searchData", new SearchData());
        return "/management/meds/search";
    }

    // POST: Search Meds
    @RequestMapping(value = "/management/meds/search", method = RequestMethod.POST)
    public String postSearchMeds(Model model, SearchData searchData)
    {
        model.addAttribute("searchData", searchData);
        String search = "";
        if(searchData.getValue().isEmpty())
            search = "/all/all";
        else
            search = "/" + searchData.getOption() + "/" + searchData.getValue();
        return "redirect:/management/meds/search" + search;
    }

    // GET: SearchScore Meds
    @RequestMapping(value = "/management/meds/search/{option}/{value}", method = RequestMethod.GET)
    public String getSearchMeds(@PathVariable(name = "option", value = "option") String option, @PathVariable(name = "value", value = "value") String value, Model model)
    {
        List<Meds> meds = new ArrayList<Meds>();
        if(option.equals("all"))
        {
            meds = medsRepository.findAll();
        }
        else if(option.equals("1"))
        {
            try {
                long id = Long.parseLong(value);
                meds = medsRepository.findAllByMedicineId(id);
            }
            catch (Exception ignored) {}
        }
        else if(option.equals("2"))
        {
            meds = medsRepository.findAllByMedicineContaining(value);
        }
        else if(option.equals("3"))
        {
            try {
                meds = medsRepository.findAllByMedicinePrice(Double.parseDouble(value));
            }
            catch (Exception ignored) {}
        }
        else if(option.equals("4"))
        {
            try {
                meds = medsRepository.findAllByMedicinePriceLessThan(Double.parseDouble(value));
            }
            catch (Exception ignored) {}
        }
        else if(option.equals("5"))
        {
            try {
                meds = medsRepository.findAllByMedicinePriceGreaterThan(Double.parseDouble(value));
            }
            catch (Exception ignored) {}
        }

        model.addAttribute("meds", meds);
        return "/management/meds/index";
    }

    // GET: Add Medicine
    @RequestMapping(value = "/management/meds/add", method = RequestMethod.GET)
    public String getAddMedicine(Model model)
    {
        MedicineData medicineData = new MedicineData();
        model.addAttribute("medicineData", medicineData);
        return "/management/meds/add";
    }

    // POST: Add Medicine
    @RequestMapping(value = "/management/meds/add", method = RequestMethod.POST)
    public String postAddMedicine(MedicineData medicineData, Model model)
    {
        if (CustomValidator.medicineValidation(model, medicineData.getMedicine())
                || CustomValidator.medicinePriceValidation(model, medicineData.getMedicinePrice()))
        {
            model.addAttribute("medicineData", medicineData);
            return "/management/meds/add";
        }
        if(!medsRepository.findAllByMedicine(medicineData.getMedicine()).isEmpty())
        {
            model.addAttribute("medicineError", "Istnieje już lek o podanej nazwie");
            model.addAttribute("medicineData", medicineData);
            return "/management/meds/add";
        }
        Meds medicine = new Meds();
        medicine.setMedicine(medicineData.getMedicine());
        medicine.setMedicinePrice(Double.parseDouble(medicineData.getMedicinePrice()));
        medicine.setMedsPrescriptions(null);
        medsRepository.save(medicine);
        return "redirect:/management/meds";
    }

    // GET: Edit Medicine
    @RequestMapping(value = "/management/meds/edit/{id}", method = RequestMethod.GET)
    public String getEditMedicine(@PathVariable(name = "id", value = "id") String id, Model model)
    {
        MedicineData medicineData = new MedicineData();
        Meds medicine = null;
        try {
            medicine = medsRepository.findByMedicineId(Long.parseLong(id));
            if(medicine == null) throw new NumberFormatException("Brak");
        }
        catch (NumberFormatException exception)
        {
            return "redirect:/management/meds";
        }
        medicineData.setMedicine(medicine.getMedicine());
        medicineData.setMedicinePrice(medicine.getMedicinePrice().toString());
        medicineData.setMedicineId(id);
        model.addAttribute("medicineData", medicineData);
        return "/management/meds/edit";
    }

    // POST: Edit Medicine
    @RequestMapping(value = "/management/meds/edit", method = RequestMethod.POST)
    public String postEditMedicine(MedicineData medicineData, Model model)
    {
        Meds medicine = null;
        try {
            medicine = medsRepository.findByMedicineId(Long.parseLong(medicineData.getMedicineId()));
            if(medicine == null) throw new NumberFormatException("Brak");
        }
        catch (NumberFormatException exception)
        {
            return "redirect:/management/meds";
        }
        if (CustomValidator.medicineValidation(model, medicineData.getMedicine())
                || CustomValidator.medicinePriceValidation(model, medicineData.getMedicinePrice()))
        {
            model.addAttribute("medicineData", medicineData);
            return "/management/meds/edit";
        }
        if(!medicine.getMedicine().equals(medicineData.getMedicine()) && !medsRepository.findAllByMedicine(medicineData.getMedicine()).isEmpty())
        {
            model.addAttribute("medicineError", "Istnieje już lek o podanej nazwie");
            model.addAttribute("medicineData", medicineData);
            return "/management/meds/edit";
        }
        medicine.setMedicine(medicineData.getMedicine());
        medicine.setMedicinePrice(Double.parseDouble(medicineData.getMedicinePrice()));
        medicine.setMedsPrescriptions(null);
        medsRepository.save(medicine);
        return "redirect:/management/meds";
    }

    // GET: Medicine Details
    @RequestMapping(value = "/management/meds/{id}", method = RequestMethod.GET)
    public String getMedicineDetails(@PathVariable(name = "id", value = "id") String id, Model model)
    {
        try {
            Meds medicine = medsRepository.findByMedicineId(Long.parseLong(id));
            model.addAttribute("medicine", medicine);
            if(medicine == null) throw new NumberFormatException("Brak");
        }
        catch (NumberFormatException exception)
        {
            return "redirect:/management/meds";
        }
        return "/management/meds/details";
    }

    // GET: Treatments
    @RequestMapping(value = "/management/treatments", method = RequestMethod.GET)
    public String getTreatments(Model model)
    {
        model.addAttribute("treatments", treatmentsRepository.findAll());
        return "/management/treatments/index";
    }

    // GET: Search Treatments
    @RequestMapping(value = "/management/treatments/search", method = RequestMethod.GET)
    public String getSearchTreatments(Model model)
    {
        model.addAttribute("searchData", new SearchData());
        return "/management/treatments/search";
    }

    // POST: Search Treatments
    @RequestMapping(value = "/management/treatments/search", method = RequestMethod.POST)
    public String postSearchTreatments(Model model, SearchData searchData)
    {
        model.addAttribute("searchData", searchData);
        String search = "";
        if(searchData.getValue().isEmpty())
            search = "/all/all";
        else
            search = "/" + searchData.getOption() + "/" + searchData.getValue();
        return "redirect:/management/treatments/search" + search;
    }

    // GET: SearchScore Treatments
    @RequestMapping(value = "/management/treatments/search/{option}/{value}", method = RequestMethod.GET)
    public String getSearchTreatments(@PathVariable(name = "option", value = "option") String option, @PathVariable(name = "value", value = "value") String value, Model model)
    {
        List<Treatments> treatments = new ArrayList<Treatments>();
        if(option.equals("all"))
        {
           treatments = treatmentsRepository.findAll();
        }
        else if(option.equals("1"))
        {
            try {
                long id = Long.parseLong(value);
                treatments = treatmentsRepository.findAllByTreatmentId(id);
            }
            catch (Exception ignored) {}
        }
        else if(option.equals("2"))
        {
            treatments = treatmentsRepository.findAllByTreatmentContaining(value);
        }
        else if(option.equals("3"))
        {
            try {
                treatments = treatmentsRepository.findAllByTreatmentPrice(Double.parseDouble(value));
            }
            catch (Exception ignored) {}
        }
        else if(option.equals("4"))
        {
            try {
                treatments = treatmentsRepository.findAllByTreatmentPriceLessThan(Double.parseDouble(value));
            }
            catch (Exception ignored) {}
        }
        else if(option.equals("5"))
        {
            try {
                treatments = treatmentsRepository.findAllByTreatmentPriceGreaterThan(Double.parseDouble(value));
            }
            catch (Exception ignored) {}
        }
        else if(option.equals("6"))
        {
            try {
                List<Species> species = speciesRepository.findAllBySpeciesContaining(value);
                List<TreatmentsSpecies> treatmentsSpecies = treatmentsSpeciesRepository.findAllBySpeciesIdIn(species);
                List<Long> id = new ArrayList<Long>();
                for (TreatmentsSpecies treatmentsSpecies1 : treatmentsSpecies) {
                    id.add(treatmentsSpecies1.getTreatmentId().getTreatmentId());
                }
                treatments = treatmentsRepository.findAllByTreatmentIdIn(id);
            }
            catch (Exception ignored) {}
        }

        model.addAttribute("treatments", treatments);
        return "/management/treatments/index";
    }

    // GET: Add treatment
    @RequestMapping(value = "/management/treatments/add", method = RequestMethod.GET)
    public String getAddTreatment(Model model)
    {
        TreatmentData treatmentData = new TreatmentData();
        model.addAttribute("treatmentData", treatmentData);
        return "/management/treatments/add";
    }

    // POST: Add treatment
    @RequestMapping(value = "/management/treatments/add", method = RequestMethod.POST)
    public String postAddTreatment(TreatmentData treatmentData, Model model)
    {
        if (CustomValidator.treatmentValidation(model, treatmentData.getTreatment())
                || CustomValidator.treatmentPriceValidation(model, treatmentData.getTreatmentPrice()))
        {
            model.addAttribute("treatmentData", treatmentData);
            return "/management/treatments/add";
        }
        if(!treatmentsRepository.findByTreatment(treatmentData.getTreatment()).isEmpty())
        {
            model.addAttribute("treatmentError", "Istnieje już zabieg o podanej nazwie");
            model.addAttribute("treatmentData", treatmentData);
            return "/management/treatments/add";
        }
        Treatments treatment = new Treatments();
        treatment.setTreatment(treatmentData.getTreatment());
        treatment.setTreatmentPrice(Double.parseDouble(treatmentData.getTreatmentPrice()));
        treatment.setTreatmentsSpecies(null);
        treatment.setTreatmentsVisits(null);
        treatmentsRepository.save(treatment);
        return "redirect:/management/treatments";
    }

    // GET: Add species to treatment
    @RequestMapping(value = "/management/treatments/addSpecies/{id}", method = RequestMethod.GET)
    public String getAddTreatmentSpecies(@PathVariable(name = "id", value = "id") String id, Model model)
    {
        TreatmentSpeciesData treatmentSpeciesData = new TreatmentSpeciesData();
        Treatments treatment = null;
        try
        {
            treatment = treatmentsRepository.findByTreatmentId(Long.parseLong(id));
            if (treatment == null) throw new NumberFormatException("Brak");
        }
        catch (NumberFormatException exception)
        {
            return "redirect:/management/treatments";
        }
        treatmentSpeciesData.setTreatmentId(id);
        model.addAttribute("treatmentSpeciesData", treatmentSpeciesData);
        model.addAttribute("species", speciesRepository.findAll());
        return "/management/treatments/addSpecies";
    }

    // POST: Add species to treatment
    @RequestMapping(value = "/management/treatments/addSpecies", method = RequestMethod.POST)
    public String postAddTreatmentSpecies(TreatmentSpeciesData treatmentSpeciesData, Model model)
    {
        TreatmentsSpecies treatmentsSpecies = new TreatmentsSpecies();
        Treatments treatment = null;
        Species species = null;
        try
        {
            treatment = treatmentsRepository.findByTreatmentId(Long.parseLong(treatmentSpeciesData.getTreatmentId()));
            species = speciesRepository.findBySpeciesId(Long.parseLong(treatmentSpeciesData.getSpeciesId()));
            if (treatment == null || species == null) throw new NumberFormatException("Brak");
        }
        catch (NumberFormatException exception)
        {
            return "redirect:/management/treatments";
        }
        treatmentsSpecies.setSpeciesId(species);
        treatmentsSpecies.setTreatmentId(treatment);
        treatmentsSpeciesRepository.save(treatmentsSpecies);
        treatment.getTreatmentsSpecies().add(treatmentsSpecies);
        treatmentsRepository.save(treatment);
        species.getTreatmentsSpecies().add(treatmentsSpecies);
        speciesRepository.save(species);
        return "redirect:/management/treatments/" + treatment.getTreatmentId();
    }

    // GET: Edit treatment
    @RequestMapping(value = "/management/treatments/edit/{id}", method = RequestMethod.GET)
    public String getEditTreatment(@PathVariable(name = "id", value = "id") String id, Model model)
    {
        TreatmentData treatmentData = new TreatmentData();
        Treatments treatment = null;
        try {
            treatment = treatmentsRepository.findByTreatmentId(Long.parseLong(id));
            if (treatment == null) throw new NumberFormatException("Brak");
        }
        catch (NumberFormatException exception)
        {
            return "redirect:/management/treatments";
        }
        treatmentData.setTreatment(treatment.getTreatment());
        treatmentData.setTreatmentPrice(treatment.getTreatmentPrice().toString());
        treatmentData.setTreatmentId(id);
        model.addAttribute("treatmentData", treatmentData);
        return "/management/treatments/edit";
    }

    // POST: Edit treatment
    @RequestMapping(value = "/management/treatments/edit", method = RequestMethod.POST)
    public String postEditTreatment(TreatmentData treatmentData, Model model)
    {
        Treatments treatment = null;
        try {
            treatment = treatmentsRepository.findByTreatmentId(Long.parseLong(treatmentData.getTreatmentId()));
            if (treatment == null) throw new NumberFormatException("Brak");
        }
        catch (NumberFormatException exception)
        {
            return "redirect:/management/treatments";
        }
        if (CustomValidator.treatmentValidation(model, treatmentData.getTreatment())
                || CustomValidator.treatmentPriceValidation(model, treatmentData.getTreatmentPrice()))
        {
            model.addAttribute("treatmentData", treatmentData);
            return "/management/meds/edit";
        }
        if(!treatment.getTreatment().equals(treatmentData.getTreatment()) && !treatmentsRepository.findByTreatment(treatmentData.getTreatment()).isEmpty())
        {
            model.addAttribute("treatmentError", "Istnieje już lek o podanej nazwie");
            model.addAttribute("treatmentData", treatmentData);
            return "/management/treatments/edit";
        }
        treatment.setTreatment(treatmentData.getTreatment());
        treatment.setTreatmentPrice(Double.parseDouble(treatmentData.getTreatmentPrice()));
        treatment.setTreatmentsVisits(null);
        treatment.setTreatmentsSpecies(null);
        treatmentsRepository.save(treatment);
        return "redirect:/management/treatments";
    }

    // GET: Treatment details
    @RequestMapping(value = "/management/treatments/{id}", method = RequestMethod.GET)
    public String getTreatmentDetails(@PathVariable(name = "id", value = "id") String id, Model model)
    {
        try {
            Treatments treatment = treatmentsRepository.findByTreatmentId(Long.parseLong(id));
            model.addAttribute("treatment", treatment);
            if (treatment == null) throw new NumberFormatException("Brak");
        }
        catch (NumberFormatException exception)
        {
            return "redirect:/management/treatments";
        }
        return "/management/treatments/details";
    }
}
