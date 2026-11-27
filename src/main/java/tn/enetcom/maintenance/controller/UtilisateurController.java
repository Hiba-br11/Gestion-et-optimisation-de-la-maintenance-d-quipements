package tn.enetcom.maintenance.controller;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import tn.enetcom.maintenance.dto.UtilisateurModification;
import tn.enetcom.maintenance.dto.UtilisateurRequest;
import tn.enetcom.maintenance.dto.UtilisateurResponse;
import tn.enetcom.maintenance.service.UtilisateurService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/utilisateurs")
@RequiredArgsConstructor

public class UtilisateurController {
    private final UtilisateurService service;

    @GetMapping
    public List<UtilisateurResponse> lister() {
        return service.lister();
    }

    @GetMapping("/{id}")
    public UtilisateurResponse obtenir(@PathVariable Long id) {
        return service.obtenir(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UtilisateurResponse creer(@Valid @RequestBody UtilisateurRequest r) {
        return service.creer(r);
    }

    @PutMapping("/{id}")
    public UtilisateurResponse modifier(@PathVariable Long id,
                                        @Valid @RequestBody UtilisateurModification r) {
        return service.modifier(id, r);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable Long id) {
        service.supprimer(id);
    }
}
