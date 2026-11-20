package tn.enetcom.maintenance.controller;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import tn.enetcom.maintenance.model.Technicien;
import tn.enetcom.maintenance.service.TechnicienService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.enetcom.maintenance.model.Indisponibilite;

import java.util.List;

@RestController
@RequestMapping("/api/techniciens")
@RequiredArgsConstructor

public class TechnicienController {
    private final TechnicienService service;

    @GetMapping
    public List<Technicien> lister() {
        return service.lister();
    }

    @GetMapping("/{id}")
    public Technicien trouver(@PathVariable Long id) {
        return service.trouver(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Technicien creer(@Valid @RequestBody Technicien t) {
        return service.creer(t);
    }

    @PutMapping("/{id}")
    public Technicien modifier(@PathVariable Long id, @Valid @RequestBody Technicien t) {
        return service.modifier(id, t);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable Long id) {
        service.supprimer(id);
    }

    @PostMapping("/{id}/indisponibilites")
    @ResponseStatus(HttpStatus.CREATED)
    public Indisponibilite ajouterIndisponibilite(@PathVariable Long id,
                                                  @Valid @RequestBody IndisponibiliteRequest r) {
        return service.ajouterIndisponibilite(id, r.date(), r.motif());
    }

    @DeleteMapping("/{id}/indisponibilites/{indispoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimerIndisponibilite(@PathVariable Long id, @PathVariable Long indispoId) {
        service.supprimerIndisponibilite(id, indispoId);
    }
}
