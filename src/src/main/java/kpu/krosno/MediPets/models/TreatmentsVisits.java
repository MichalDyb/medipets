package kpu.krosno.MediPets.models;

import lombok.Data;
import javax.persistence.*;
import java.util.Objects;

@Entity
@IdClass(TreatmentsVisitsKey.class)
@Data
public class TreatmentsVisits {
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="treatmentId", nullable=false)
    private Treatments treatmentId;
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="visitId", nullable=false)
    private Visits visitId;

    public TreatmentsVisits() {
    }

    public TreatmentsVisits(Treatments treatmentId, Visits visitId) {
        this.treatmentId = treatmentId;
        this.visitId = visitId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTreatmentId(), getVisitId());
    }
}