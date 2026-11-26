package tn.enetcom.maintenance.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tn.enetcom.maintenance.model.Role;

public record UtilisateurModification(
        @NotBlank(message = "Le prénom est obligatoire") String prenom,
        @NotBlank(message = "L'e-mail est obligatoire")
        @Email(message = "L'e-mail n'est pas valide") String email,
        String motDePasse,
        @NotNull(message = "Le rôle est obligatoire") Role role,
        Long technicienId) {
}
