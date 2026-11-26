package tn.enetcom.maintenance.dto;
import tn.enetcom.maintenance.model.Role;

public record UtilisateurResponse(
        Long id,
        String prenom,
        String email,
        Role role,
        Long technicienId) {
}
