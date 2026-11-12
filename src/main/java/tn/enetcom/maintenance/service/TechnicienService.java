package tn.enetcom.maintenance.service;
import lombok.RequiredArgsConstructor;
import tn.enetcom.maintenance.model.Technicien;
import tn.enetcom.maintenance.repository.TechnicienRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor

public class TechnicienService {
    private final TechnicienRepository repo;

    public List<Technicien> lister() {
        return repo.findAll();
    }

    public Technicien trouver(Long id) {
        return repo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Technicien introuvable"));
    }

    public Technicien creer(Technicien t) {
        return repo.save(t);
    }

    public Technicien modifier(Long id, Technicien donnees) {
        Technicien t = trouver(id);
        t.setPrenom(donnees.getPrenom());
        t.getCompetences().clear();
        t.getCompetences().addAll(donnees.getCompetences());
        return repo.save(t);
    }

    public void supprimer(Long id) {
        repo.delete(trouver(id));
    }
}

