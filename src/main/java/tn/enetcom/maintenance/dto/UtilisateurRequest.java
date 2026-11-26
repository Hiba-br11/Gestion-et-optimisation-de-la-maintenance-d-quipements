package tn.enetcom.maintenance.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import tn.enetcom.maintenance.model.Role;

public record UtilisateurRequest(
    @NotBlank(message = "Le prénom est obligatoire") String prenom,
    @NotBlank(message = "L'e-mail est obligatoire")
    @Email(message = "L'e-mail n'est pas valide") String email,
    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères") String motDePasse,
    @NotNull(message = "Le rôle est obligatoire") Role role,
    Long technicienId) {
}
