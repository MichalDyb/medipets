package kpu.krosno.MediPets.models;

import lombok.Data;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
public class Treatments {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long treatmentId;
    @Column(nullable = false, length = 60)
    private String treatment;
    @Column(nullable = false)
    private Double treatmentPrice;
    @OneToMany(mappedBy = "treatmentId", fetch = FetchType.EAGER)
    private Set<TreatmentsSpecies> treatmentsSpecies;
    @OneToMany(mappedBy = "treatmentId", fetch = FetchType.EAGER)
    private Set<TreatmentsVisits> treatmentsVisits;

    public Treatments() {
        treatmentsSpecies = new HashSet<>();
        treatmentsVisits = new HashSet<>();
    }

    public Treatments(String treatment, Double treatmentPrice) {
        this.treatment = treatment;
        this.treatmentPrice = treatmentPrice;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTreatment(), getTreatmentId(), getTreatmentPrice());
    }

    @Override
    public String toString() {
        return "Treatments{" +
                "treatmentId=" + treatmentId +
                ", treatment='" + treatment + '\'' +
                ", treatmentPrice=" + treatmentPrice +
                '}';
    }
}
