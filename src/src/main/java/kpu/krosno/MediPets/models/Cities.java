package kpu.krosno.MediPets.models;

import lombok.Data;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
public class Cities {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long cityId;
    @Column(nullable = false, length = 6)
    private String postcode;
    @Column(nullable = false, length = 50)
    private String city;
    @OneToMany(mappedBy="cityId", fetch = FetchType.EAGER)
    private Set<Persons> persons;

    public Cities() {
        persons = new HashSet<>();
    }

    public Cities(String postcode, String city) {
        this.postcode = postcode;
        this.city = city;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCity(), getCityId(), getPostcode());
    }
}
