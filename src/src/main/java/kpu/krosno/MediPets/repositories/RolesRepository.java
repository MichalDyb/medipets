package kpu.krosno.MediPets.repositories;

import kpu.krosno.MediPets.models.AccountsRoles;
import kpu.krosno.MediPets.models.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Long> {
    Set<Roles> findDinstinctByRoleIdIn(Set<Long> accountsRoles);
    Roles findByRole(String role);
}
