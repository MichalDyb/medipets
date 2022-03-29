package kpu.krosno.MediPets.repositories;

import kpu.krosno.MediPets.models.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountsRepository extends JpaRepository<Accounts, Long> {
    List<Accounts> findAllByEmailAndIsEnabled(String username, boolean isEnabled);
    List<Accounts> findAllByEmail(String username);
    List<Accounts> findAllByEmailContaining(String email);
    Accounts findByEmailAndIsEnabled(String email, boolean isEnabled);
}
