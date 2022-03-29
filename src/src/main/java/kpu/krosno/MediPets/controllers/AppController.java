package kpu.krosno.MediPets.controllers;

import kpu.krosno.MediPets.models.*;
import kpu.krosno.MediPets.repositories.*;
import kpu.krosno.MediPets.security.AccountDetails;
import kpu.krosno.MediPets.utility.CustomValidator;
import kpu.krosno.MediPets.utility.PersonData;
import kpu.krosno.MediPets.utility.RegisterData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class AppController {
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


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getIndex(Model model)
    {
        if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken))
        {
            try
            {
                AccountDetails user = (AccountDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                model.addAttribute("loggedIn", true);
                model.addAttribute("userName", user.getUsername());
            }
            catch (ClassCastException exception)
            {
                model.addAttribute("loggedIn", false);
            }
        }
        else
        {
            model.addAttribute("loggedIn", false);
        }
        return "index";
    }

    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public String getAccount(Model model)
    {
        AccountDetails user = (AccountDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Persons person = accountsRepository.findAllByEmailAndIsEnabled(user.getUsername(), true).get(0).getPersonId();
        PersonData personData = new PersonData();
        personData.setPostCode(person.getCityId().getPostcode());
        personData.setIsStandardAccount(person.getIsStandardAccount().toString());
        personData.setSurname(person.getSurname());
        personData.setTelephone(person.getTelephone());
        personData.setPesel(person.getPesel());
        personData.setName(person.getName());
        personData.setCity(person.getCityId().getCity());
        personData.setAddress(person.getAddress());
        model.addAttribute("personData", personData);
        return "account";
    }

    @RequestMapping(value = "/account", method = RequestMethod.POST)
    public String postAccount(PersonData personData, Model model)
    {
        if (CustomValidator.nameValidation(model, personData.getName())
                || CustomValidator.surnameValidation(model, personData.getSurname())
                || CustomValidator.peselValidation(model, personData.getPesel())
                || CustomValidator.phoneNumberValidation(model, personData.getTelephone())
                || CustomValidator.postcodeValidation(model, personData.getPostCode())
                || CustomValidator.cityValidation(model, personData.getCity())
                || CustomValidator.streetValidation(model, personData.getAddress()))
        {
            model.addAttribute("personData", personData);
            return "account";
        }
        AccountDetails user = (AccountDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Persons person = accountsRepository.findAllByEmailAndIsEnabled(user.getUsername(), true).get(0).getPersonId();
        person.setName(personData.getName());
        person.setPesel(personData.getPesel());
        person.setAddress(personData.getAddress());
        person.setSurname(personData.getSurname());
        person.setTelephone(personData.getTelephone());
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
        return "redirect:/";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String getRegister(Model model)
    {
        RegisterData registerData = new RegisterData();
        model.addAttribute("register", registerData);
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String postRegister(HttpServletRequest request, Model model, RegisterData registerData)
    {
        if (CustomValidator.emailValidation(model, registerData.getEmail())
                || CustomValidator.passwordValidation(model, registerData.getPassword1())
                || CustomValidator.passwordConfirmValidation(model, registerData.getPassword2(), registerData.getPassword1())
                || CustomValidator.nameValidation(model, registerData.getName())
                || CustomValidator.surnameValidation(model, registerData.getSurname())
                || CustomValidator.peselValidation(model, registerData.getPesel())
                || CustomValidator.phoneNumberValidation(model, registerData.getTelephone())
                || CustomValidator.postcodeValidation(model, registerData.getPostCode())
                || CustomValidator.cityValidation(model, registerData.getCity())
                || CustomValidator.streetValidation(model, registerData.getAddress()))
        {
            model.addAttribute("register", registerData);
            return "register";
        }
        if(!accountsRepository.findAllByEmail(registerData.getEmail()).isEmpty())
        {
            model.addAttribute("emailError", "Podany adres e-mail jest już zajęty.");
            model.addAttribute("register", registerData);
            return "register";
        }
        Accounts account = new Accounts();
        account.setEmail(registerData.getEmail());
        account.setPassword(new BCryptPasswordEncoder().encode(registerData.getPassword1()));
        account.setEnabled(true);
        account.setExpired(false);
        account.setLocked(false);
        account.setCredentialsExpired(false);
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
        Persons person = new Persons();
        person.setName(registerData.getName());
        person.setAccountId(account);
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
        account.setPersonId(person);
        accountsRepository.save(account);

        try {
            request.login(account.getEmail(), registerData.getPassword1());
        } catch (ServletException e) {
            return "redirect:/";
        }

        return "redirect:/";
    }
}
