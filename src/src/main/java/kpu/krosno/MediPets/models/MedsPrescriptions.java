package kpu.krosno.MediPets.models;

import lombok.Data;
import javax.persistence.*;

@Entity
@IdClass(MedsPrescriptionsKey.class)
@Data
public class MedsPrescriptions {
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="medicineId", nullable=false)
    private Meds medicineId;
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="prescriptionId", nullable=false)
    private Prescriptions prescriptionId;
    @Column(nullable = false)
    private Integer medQuantity;
    @Column(nullable = false, length = 80)
    private String prescriptionDescription;

    public MedsPrescriptions() {
    }

    public MedsPrescriptions(Meds medicineId, Prescriptions prescriptionId, Integer medQuantity, String prescriptionDescription) {
        this.medicineId = medicineId;
        this.prescriptionId = prescriptionId;
        this.medQuantity = medQuantity;
        this.prescriptionDescription = prescriptionDescription;
    }
}



