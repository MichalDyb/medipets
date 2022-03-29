package kpu.krosno.MediPets.models;

import lombok.Data;
import org.hibernate.annotations.ManyToAny;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
public class Persons {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long personId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cityId", nullable = false)
    private Cities cityId;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "accountId")
    private Accounts accountId;
    @Column(nullable = false, length = 30)
    private String name;
    @Column(nullable = false, length = 30)
    private String surname;
    @Column(nullable = false, length = 11)
    private String pesel;
    @Column(nullable = false, length = 9)
    private String telephone;
    @Column(nullable = false, length = 75)
    private String address;
    private Boolean isStandardAccount;
    @OneToMany(mappedBy = "personId", fetch = FetchType.EAGER)
    private Set<Pets> pets;
    @OneToMany(mappedBy = "doctorId", fetch = FetchType.EAGER)
    private Set<Prescriptions> prescriptionsDoctors;
    @OneToMany(mappedBy = "clientId", fetch = FetchType.EAGER)
    private Set<Prescriptions> prescriptionsClients;

    public Persons() {
        pets = new HashSet<>();
        prescriptionsDoctors = new HashSet<>();
        prescriptionsClients = new HashSet<>();
        isStandardAccount = true;
    }

    public Persons(Cities cityId, Accounts accountId, String name, String surname, String pesel, String telephone, String address, Boolean isStandardAccount) {
        this.cityId = cityId;
        this.accountId = accountId;
        this.name = name;
        this.surname = surname;
        this.pesel = pesel;
        this.telephone = telephone;
        this.address = address;
        this.isStandardAccount = isStandardAccount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPersonId(), getAddress(), getPesel(), getCityId(), getName(), getSurname(), getTelephone());
    }
}
