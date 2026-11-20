package tn.enetcom.maintenance.repository;
import tn.enetcom.maintenance.model.Indisponibilite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface IndisponibiliteRepository extends JpaRepository<Indisponibilite, Long> {
    boolean existsByTechnicienIdAndDate(Long technicienId, LocalDate date);
}
