package kpu.krosno.MediPets.utility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisitData {
    private String visitId;
    private String petId;
    private String visitDate;
    private String visitTime;
    private String visitPrice;
    private String isPaid;
    private String visitStatus;

}
