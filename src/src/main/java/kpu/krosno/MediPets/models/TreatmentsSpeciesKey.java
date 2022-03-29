package kpu.krosno.MediPets.models;

import lombok.Data;
import java.io.Serializable;

@Data
public class TreatmentsSpeciesKey implements Serializable {
    private Long treatmentId;
    private Long speciesId;
}
