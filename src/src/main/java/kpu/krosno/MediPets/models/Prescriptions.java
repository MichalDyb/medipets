package kpu.krosno.MediPets.models;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
public class Prescriptions {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long prescriptionId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "visitId", nullable = false)
    private Visits visitId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctorId", nullable = false)
    private Persons doctorId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "clientId", nullable = false)
    private Persons clientId;
    @Column(nullable = false)
    private Date prescriptionDate;
    @OneToMany(mappedBy="prescriptionId", fetch = FetchType.EAGER)
    private Set<MedsPrescriptions> medsPrescriptions;

    public Prescriptions() {
        medsPrescriptions = new HashSet<>();
    }

    public Prescriptions(Visits visitId, Persons doctorId, Persons clientId, Date prescriptionDate) {
        this.visitId = visitId;
        this.doctorId = doctorId;
        this.clientId = clientId;
        this.prescriptionDate = prescriptionDate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClientId(), getPrescriptionDate(), getPrescriptionId(), getDoctorId(), getVisitId());
    }
}
