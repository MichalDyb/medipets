package kpu.krosno.MediPets.models;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
public class Pets {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long petId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "breedId")
    private Breeds breedId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "personId", nullable = false)
    private Persons personId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "speciesId", nullable = false)
    private Species speciesId;
    @Column(nullable = false, length = 30)
    private String petName;
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date dateOfBirth;
    @Column(nullable = false, length = 20)
    private String gender;
    @Column(length = 80)
    private String petDescription;
    @OneToMany(mappedBy = "petId", fetch = FetchType.EAGER)
    private Set<Visits> visits;

    public Pets() {
        visits = new HashSet<>();
    }

    public Pets(Breeds breedId, Persons personId, Species speciesId, String petName, Date dateOfBirth, String gender, String petDescription) {
        this.breedId = breedId;
        this.personId = personId;
        this.speciesId = speciesId;
        this.petName = petName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.petDescription = petDescription;
    }

    public Pets(Persons personId, Species speciesId, String petName, Date dateOfBirth, String gender, String petDescription) {
        this.breedId = null;
        this.personId = personId;
        this.speciesId = speciesId;
        this.petName = petName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.petDescription = petDescription;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBreedId(), getPersonId(), getPetDescription(), getDateOfBirth(), getGender(), getPetId(), getPetName(), getSpeciesId());
    }
}
