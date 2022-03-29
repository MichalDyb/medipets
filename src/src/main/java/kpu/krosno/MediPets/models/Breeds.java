package kpu.krosno.MediPets.models;

import lombok.Data;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
public class Breeds {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long breedId;
    @Column(nullable = false, length = 30)
    private String breed;
    @OneToMany(mappedBy = "breedId", fetch = FetchType.EAGER)
    private Set<Pets> pets;

    public Breeds() {
        pets = new HashSet<>();
    }

    public Breeds(String breed) {
        this.breed = breed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBreed(), getBreedId());
    }
}
