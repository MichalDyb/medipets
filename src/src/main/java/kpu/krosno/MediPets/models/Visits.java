package kpu.krosno.MediPets.models;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
public class Visits {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long visitId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "petId", nullable = false)
    private Pets petId;
    @Column(nullable = false)
    private Date visitDate;
    @Column(nullable = false)
    private Double visitPrice;
    @Column(nullable = false)
    private Boolean isPaid;
    @Column(nullable = false, length = 30)
    private String visitStatus;
    @OneToMany(mappedBy = "visitId", fetch = FetchType.EAGER)
    private Set<Prescriptions> prescriptions;
    @OneToMany(mappedBy = "visitId", fetch = FetchType.EAGER)
    private Set<TreatmentsVisits> treatmentsVisits;

    public Visits() {
        prescriptions = new HashSet<>();
        treatmentsVisits = new HashSet<>();
    }

    public Visits(Pets petId, Date visitDate, Double visitPrice, Boolean isPaid, String visitStatus) {
        this.petId = petId;
        this.visitDate = visitDate;
        this.visitPrice = visitPrice;
        this.isPaid = isPaid;
        this.visitStatus = visitStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getVisitDate(), getIsPaid(), getPetId(), getVisitStatus(), getVisitId(), getVisitPrice());
    }
}
