package kpu.krosno.MediPets.repositories;

import kpu.krosno.MediPets.models.AccountRoleKey;
import kpu.krosno.MediPets.models.Accounts;
import kpu.krosno.MediPets.models.AccountsRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Set;

@Repository
public interface AccountsRolesRepository extends JpaRepository<AccountsRoles, AccountRoleKey> {
    Set<AccountsRoles> findAllByAccountId(Accounts account);
}
