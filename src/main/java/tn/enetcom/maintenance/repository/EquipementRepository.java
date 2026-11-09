package tn.enetcom.maintenance.repository;

import tn.enetcom.maintenance.model.Equipement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipementRepository extends JpaRepository<Equipement, Long> {
}
