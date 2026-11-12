package optimisation.modele;

import java.time.LocalDate;
import java.util.List;

public class Planification {
    private final LocalDate jour;
    private final List<Affectation> affectations;
    private final List<Demande> nonAffectees;
    private final int valeurTotale;

    public Planification(LocalDate jour, List<Affectation> affectations,List<Demande> nonAffectees, int valeurTotale) {
        this.jour = jour;
        this.affectations = List.copyOf(affectations);
        this.nonAffectees = List.copyOf(nonAffectees);
        this.valeurTotale = valeurTotale;
    }

    public LocalDate getJour() { 
    	return jour; 
    }
    public List<Affectation> getAffectations() {
    	return affectations; 
    }
    public List<Demande> getNonAffectees() { 
    	return nonAffectees; 
    }
    public int getValeurTotale() { 
    	return valeurTotale; 
    }

    public int getNbCritiquesNonTraitees() {
        return (int) nonAffectees.stream().filter(Demande::estCritique).count();
    }
    
    @Override
    public String toString() {
        return "Planification " + jour + " : " + affectations
                + " | non affectées : " + nonAffectees
                + " | valeur = " + valeurTotale;
    }
    
}
