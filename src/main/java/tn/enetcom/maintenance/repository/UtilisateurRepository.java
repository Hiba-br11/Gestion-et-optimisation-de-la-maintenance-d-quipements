package tn.enetcom.maintenance.repository;
import tn.enetcom.maintenance.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Optional<Utilisateur> findByEmail(String email);

    Optional<Utilisateur> findByTechnicienId(Long technicienId);
}
