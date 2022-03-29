package kpu.krosno.MediPets.models;

import lombok.Data;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
public class Species {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long speciesId;
    @Column(nullable = false, length = 30)
    private String species;
    @OneToMany(mappedBy = "speciesId", fetch = FetchType.EAGER)
    private Set<Pets> pets;
    @OneToMany(mappedBy = "speciesId", fetch = FetchType.EAGER)
    private Set<TreatmentsSpecies> treatmentsSpecies;

    public Species() {
        pets = new HashSet<>();
        treatmentsSpecies = new HashSet<>();
    }

    public Species(String species) {
        this.species = species;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSpecies(), getSpeciesId());
    }

    @Override
    public String toString() {
        return "Species{" +
                "speciesId=" + speciesId +
                ", species='" + species + '\'' +
                '}';
    }
}
