package tn.enetcom.maintenance.service;
import lombok.RequiredArgsConstructor;
import tn.enetcom.maintenance.model.Equipement;
import tn.enetcom.maintenance.repository.EquipementRepository;

//permettant de gérer les erreurs HTTP(comme erreur 404)
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
@RequiredArgsConstructor //génére automatiquement un constructeur car repo declaré final

public class EquipementService {
    private final EquipementRepository repo; //permet de communiquer avec BD

    public List<Equipement> lister() {
        return repo.findAll(); //recupere tous les équipements
    }

    //recherche un equipement à partir de son id s'il trouve sinon il va afficher Equipement introuvable
    public Equipement trouver(Long id) {
        return repo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Équipement introuvable"));
    }

    //Ajout d'un nouveau equipement
    public Equipement creer(Equipement e) {
        return repo.save(e); //Enregistre equipement dans la BD
    }

    //Modifier un equipement existant
    public Equipement modifier(Long id, Equipement donnees) {
        Equipement e = trouver(id); //recherche equipement avec id
        e.setDesignation(donnees.getDesignation()); //on remplace l'ancienne designation par la nouvelle
        e.setLieu(donnees.getLieu()); //remplace nouveau lien par le nouveau
        return repo.save(e); //on sauvegarde les modifications dans BD
    }

    //Supprimer un equipement
    public void supprimer(Long id) {
        repo.delete(trouver(id));
    }
}

