package kpu.krosno.MediPets.models;

import lombok.Data;
import java.io.Serializable;

@Data
public class MedsPrescriptionsKey implements Serializable {
    private Long medicineId;
    private Long prescriptionId;
}
