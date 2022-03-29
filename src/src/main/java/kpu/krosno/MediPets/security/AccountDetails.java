package kpu.krosno.MediPets.security;

import kpu.krosno.MediPets.models.Accounts;
import kpu.krosno.MediPets.models.AccountsRoles;
import kpu.krosno.MediPets.models.Roles;
import kpu.krosno.MediPets.repositories.AccountsRolesRepository;
import kpu.krosno.MediPets.repositories.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class AccountDetails implements UserDetails {

    private RolesRepository rolesRepository;
    private AccountsRolesRepository accountsRolesRepository;
    private Accounts account;

    public AccountDetails(Accounts account, RolesRepository rolesRepository, AccountsRolesRepository accountsRolesRepository) {
        this.account = account;
        this.rolesRepository = rolesRepository;
        this.accountsRolesRepository = accountsRolesRepository;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<AccountsRoles> accountsRoles = accountsRolesRepository.findAllByAccountId(account);
        Set<Long> roleIds = new HashSet<>();
        for (AccountsRoles accountRole: accountsRoles) {
            roleIds.add(accountRole.getRoleId().getRoleId());
        }
        Set<Roles> roles = rolesRepository.findDinstinctByRoleIdIn(roleIds);

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Roles role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRole()));
        }

        return authorities;
    }

    @Override
    public String getUsername() { return account.getEmail(); }

    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return !account.isExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !account.isLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !account.isCredentialsExpired();
    }

    @Override
    public boolean isEnabled() {
        return account.isEnabled();
    }


}

