package tn.enetcom.maintenance;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccueilController {

    @GetMapping("/")
    public String accueil() {
        return "acceuil";
    }

    @GetMapping("/connexion")
    public String connexion() {
        return "connexion";
    }

    @GetMapping("/inscription")
    public String inscription() {
        return "inscription";
    }
}