package kpu.krosno.MediPets.models;

import lombok.Data;
import javax.persistence.*;
import java.util.Objects;

@Entity
@IdClass(TreatmentsSpeciesKey.class)
@Data
public class TreatmentsSpecies {
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="treatmentId", nullable=false)
    private Treatments treatmentId;
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="speciesId", nullable=false)
    private Species speciesId;

    public TreatmentsSpecies() {
    }

    public TreatmentsSpecies(Treatments treatmentId, Species speciesId) {
        this.treatmentId = treatmentId;
        this.speciesId = speciesId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSpeciesId(), getTreatmentId());
    }
}
