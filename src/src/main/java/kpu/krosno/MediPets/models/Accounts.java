package kpu.krosno.MediPets.models;

import lombok.Data;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
public class Accounts {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long accountId;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "personId")
    private Persons personId;
    @Column(nullable = false, length = 255, unique = true)
    private String email;
    @Column(nullable = false, length = 255)
    private String password;
    @Column(nullable = false)
    private boolean isEnabled;
    @Column(nullable = false)
    private boolean isLocked;
    @Column(nullable = false)
    private boolean isExpired;
    @Column(nullable = false)
    private boolean isCredentialsExpired;
    @OneToMany(mappedBy="accountId", fetch = FetchType.EAGER)
    private Set<AccountsRoles> accountsRoles;

    public Accounts() {
        accountsRoles = new HashSet<>();
    }

    public Accounts(String email, String password) {
        this.email = email;
        this.password = password;
        isEnabled = true;
        isExpired = false;
        isLocked = false;
        isCredentialsExpired = false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAccountId(), getEmail(), getPassword(), isEnabled(), isLocked(), isExpired(), isCredentialsExpired());
    }

    @Override
    public String toString() {
        return "Accounts{" +
                "accountId=" + accountId +
                ", personId=" + personId +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", isEnabled=" + isEnabled +
                ", isLocked=" + isLocked +
                ", isExpired=" + isExpired +
                ", isCredentialsExpired=" + isCredentialsExpired +
                '}';
    }
}
