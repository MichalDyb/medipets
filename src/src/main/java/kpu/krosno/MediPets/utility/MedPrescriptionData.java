package kpu.krosno.MediPets.utility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedPrescriptionData {
    private String medicineId;
    private String prescriptionId;
    private String medQuantity;
    private String prescriptionDescription;
}
