package tn.enetcom.maintenance.service;
import lombok.RequiredArgsConstructor;
import tn.enetcom.maintenance.dto.UtilisateurModification;
import tn.enetcom.maintenance.dto.UtilisateurRequest;
import tn.enetcom.maintenance.dto.UtilisateurResponse;
import tn.enetcom.maintenance.model.Role;
import tn.enetcom.maintenance.model.Technicien;
import tn.enetcom.maintenance.model.Utilisateur;
import tn.enetcom.maintenance.repository.TechnicienRepository;
import tn.enetcom.maintenance.repository.UtilisateurRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UtilisateurService {
    private final UtilisateurRepository repo;
    private final TechnicienRepository technicienRepo;
    private final PasswordEncoder encoder;

    public List<UtilisateurResponse> lister() {
        return repo.findAll().stream().map(this::versReponse).toList();
    }

    public UtilisateurResponse obtenir(Long id) {
        return versReponse(trouver(id));
    }

    public UtilisateurResponse creer(UtilisateurRequest r) {
        repo.findByEmail(r.email()).ifPresent(autre -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cet e-mail est déjà utilisé");
        });

        Utilisateur u = new Utilisateur();
        u.setPrenom(r.prenom());
        u.setEmail(r.email());
        u.setMotDePasseHash(encoder.encode(r.motDePasse()));
        u.setRole(r.role());
        u.setTechnicien(resoudreTechnicien(r.role(), r.technicienId(), null));
        return versReponse(repo.save(u));
    }

    public UtilisateurResponse modifier(Long id, UtilisateurModification r) {
        Utilisateur u = trouver(id);

        repo.findByEmail(r.email()).ifPresent(autre -> {
            if (!autre.getId().equals(id)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Cet e-mail est déjà utilisé");
            }
        });

        u.setPrenom(r.prenom());
        u.setEmail(r.email());
        u.setRole(r.role());
        u.setTechnicien(resoudreTechnicien(r.role(), r.technicienId(), id));

        // Mot de passe : on ne le change que s'il est fourni
        if (r.motDePasse() != null && !r.motDePasse().isBlank()) {
            if (r.motDePasse().length() < 6) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Le mot de passe doit contenir au moins 6 caractères");
            }
            u.setMotDePasseHash(encoder.encode(r.motDePasse()));
        }
        return versReponse(repo.save(u));
    }

    public void supprimer(Long id) {
        repo.delete(trouver(id));
    }

    private Utilisateur trouver(Long id) {
        return repo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));
    }

    // Un compte TECHNICIEN doit pointer vers une fiche technicien libre ; les autres rôles n'en ont pas
    private Technicien resoudreTechnicien(Role role, Long technicienId, Long utilisateurId) {
        if (role != Role.TECHNICIEN) {
            return null;
        }
        if (technicienId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Un compte technicien doit référencer une fiche technicien");
        }
        Technicien t = technicienRepo.findById(technicienId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Technicien introuvable"));

        repo.findByTechnicienId(technicienId).ifPresent(autre -> {
            if (!autre.getId().equals(utilisateurId)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Ce technicien a déjà un compte");
            }
        });
        return t;
    }

    private UtilisateurResponse versReponse(Utilisateur u) {
        return new UtilisateurResponse(u.getId(), u.getPrenom(), u.getEmail(), u.getRole(),
                u.getTechnicien() != null ? u.getTechnicien().getId() : null);
    }
}
