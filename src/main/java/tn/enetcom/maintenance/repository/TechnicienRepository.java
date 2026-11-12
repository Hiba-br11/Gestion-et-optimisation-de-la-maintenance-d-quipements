package tn.enetcom.maintenance.repository;
import tn.enetcom.maintenance.model.Technicien;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TechnicienRepository extends JpaRepository<Technicien, Long> {
}
