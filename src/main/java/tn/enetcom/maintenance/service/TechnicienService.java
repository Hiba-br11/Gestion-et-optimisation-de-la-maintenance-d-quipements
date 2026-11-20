package tn.enetcom.maintenance.service;
import lombok.RequiredArgsConstructor;
import tn.enetcom.maintenance.model.Technicien;
import tn.enetcom.maintenance.repository.TechnicienRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.enetcom.maintenance.model.Indisponibilite;
import tn.enetcom.maintenance.repository.IndisponibiliteRepository;
import java.time.LocalDate;

import java.util.List;

@Service
@RequiredArgsConstructor

public class TechnicienService {
    private final TechnicienRepository repo;
    private final IndisponibiliteRepository indispoRepo;

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

    public Indisponibilite ajouterIndisponibilite(Long technicienId, LocalDate date, String motif) {
        Technicien t = trouver(technicienId);

        if (indispoRepo.existsByTechnicienIdAndDate(technicienId, date)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ce technicien est déjà indisponible à cette date");
        }

        Indisponibilite i = new Indisponibilite();
        i.setTechnicien(t);
        i.setDate(date);
        i.setMotif(motif);
        return indispoRepo.save(i);
    }

    public void supprimerIndisponibilite(Long technicienId, Long indispoId) {
        Indisponibilite i = indispoRepo.findById(indispoId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Indisponibilité introuvable"));

        if (!i.getTechnicien().getId().equals(technicienId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Cette indisponibilité n'appartient pas à ce technicien");
        }
        indispoRepo.delete(i);
    }

    public boolean estIndisponible(Long technicienId, LocalDate date) {
        return indispoRepo.existsByTechnicienIdAndDate(technicienId, date);
    }
}

