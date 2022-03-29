package kpu.krosno.MediPets.utility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentData {
    private String treatmentId;
    private String treatment;
    private String treatmentPrice;
}
