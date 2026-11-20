package tn.enetcom.maintenance.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"technicien_id", "jour"}))
@Getter
@Setter
@NoArgsConstructor

public class Indisponibilite {
    @Id //indique que id est le clé primaire
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La date est obligatoire")
    @Column(name = "jour", nullable = false)
    private LocalDate date;

    private String motif;

    @ManyToOne(optional = false)
    @JoinColumn(name = "technicien_id") //tech_id est un clé etranger
    @JsonIgnore
    private Technicien technicien;
}
