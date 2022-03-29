package kpu.krosno.MediPets.utility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionData {
    private String prescriptionId;
    private String clientId;
    private String doctorId;
    private String visitId;
    private String prescriptionDate;
}
