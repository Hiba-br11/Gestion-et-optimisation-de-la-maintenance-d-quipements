
//Role: Recevoir les requetes HTTP envoyées par le frontend
// puis les transmettere les demandes à EquipementService
package tn.enetcom.maintenance.controller;

import jakarta.validation.Valid; //Declenche la validation des données reçues
import lombok.RequiredArgsConstructor; //Genere un constructeur pour les attributs
import tn.enetcom.maintenance.model.Equipement; //Classe qui represente un equipement
import tn.enetcom.maintenance.service.EquipementService; //Classe qui contient les methodes métier
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController //reçoit des requettes HTTP et retourne les resultats au format JSON
@RequestMapping("/api/equipements") //
@RequiredArgsConstructor

public class EquipementController {
    private final EquipementService service;

    @GetMapping
    public List<Equipement> lister() {
        return service.lister();
    }  //afficher les equipement(GET)

    @GetMapping("/{id}") //id est une partie de url
    public Equipement trouver(@PathVariable Long id) {
        return service.trouver(id);
    } //recherche d'equipement par id

    @PostMapping //cette méthode repond à une requete HTTP POST
    @ResponseStatus(HttpStatus.CREATED)
    public Equipement creer(@Valid @RequestBody Equipement e) {
        return service.creer(e);
    } //Ajout un equipement

    @PutMapping("/{id}")
    public Equipement modifier(@PathVariable Long id, @Valid @RequestBody Equipement e) {
        return service.modifier(id, e); //service recherche l'equipement num id et modifie ses attributs et sauvegarde les changement
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable Long id) {
        service.supprimer(id);
    } //demande au service de supprimer un equipement
}

