package kpu.krosno.MediPets.models;

import lombok.Data;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
public class Meds {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long medicineId;
    @Column(nullable = false, length = 60)
    private String medicine;
    @Column(nullable = false)
    private Double medicinePrice;
    @OneToMany(mappedBy="medicineId", fetch = FetchType.EAGER)
    private Set<MedsPrescriptions> medsPrescriptions;

    public Meds() {
        medsPrescriptions = new HashSet<>();
    }

    public Meds(String medicine, Double medicinePrice) {
        this.medicine = medicine;
        this.medicinePrice = medicinePrice;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMedicine(), getMedicineId(), getMedicinePrice());
    }
}
