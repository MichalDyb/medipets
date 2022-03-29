package kpu.krosno.MediPets.models;

import lombok.Data;
import java.io.Serializable;

@Data
public class TreatmentsVisitsKey implements Serializable {
    private Long treatmentId;
    private Long visitId;
}