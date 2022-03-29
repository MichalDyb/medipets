package kpu.krosno.MediPets.security;

import kpu.krosno.MediPets.models.Accounts;
import kpu.krosno.MediPets.repositories.AccountsRepository;
import kpu.krosno.MediPets.repositories.AccountsRolesRepository;
import kpu.krosno.MediPets.repositories.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.List;

public class AccountDetailsService implements UserDetailsService {

    @Autowired
    private AccountsRepository accountsRepository;
    @Autowired
    private AccountsRolesRepository accountsRolesRepository;
    @Autowired
    private RolesRepository rolesRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<Accounts> accounts = accountsRepository.findAllByEmailAndIsEnabled(username, true);
        if(accounts.isEmpty()){
            throw new UsernameNotFoundException("Nie można znaleźć klienta.");
        }
        return new AccountDetails(accounts.get(0), rolesRepository, accountsRolesRepository);
    }
}
