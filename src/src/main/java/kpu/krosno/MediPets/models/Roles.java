package kpu.krosno.MediPets.models;

import lombok.Data;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long roleId;
    @Column(nullable = false, length = 20, unique = true)
    private String role;
    @OneToMany(mappedBy="roleId", fetch = FetchType.EAGER)
    private Set<AccountsRoles> accountsRoles;

    public Roles() {
        accountsRoles = new HashSet<>();
    }

    public Roles(String role) {
        this.role = role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRoleId(), getRole());
    }

    @Override
    public String toString() {
        return "Roles{" +
                "roleId=" + roleId +
                ", role='" + role + '\'' +
                '}';
    }
}
