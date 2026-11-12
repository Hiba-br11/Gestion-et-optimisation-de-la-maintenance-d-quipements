package optimisation.modele;

import java.time.LocalDate;
import java.util.Objects;

public class Demande {
    private final int id;
    private final Categorie categorie;          //final :cad vleur fixe pas modifiable
    private Priorite priorite;          // peut changer (scénario 7 : D5 passe à CRITIQUE)
    private final int duree;            // en heures, entre 1 et 8
    private final LocalDate dateSignalement;

    public Demande(int id, Categorie categorie, Priorite priorite,int duree, LocalDate dateSignalement) {
        if (duree < 1 || duree > 8) {
            throw new IllegalArgumentException("La durée doit être comprise entre 1 et 8 h");
        }
        this.id = id;
        this.categorie = Objects.requireNonNull(categorie);
        this.priorite = Objects.requireNonNull(priorite);
        this.duree = duree;
        this.dateSignalement = Objects.requireNonNull(dateSignalement);
    }

    public int getId() { 
    	return id; 
    }
    public Categorie getCategorie() { 
    	return categorie; 
    }
    public Priorite getPriorite() { 
    	return priorite; 
    }
    public void setPriorite(Priorite priorite) { 
    	this.priorite = priorite; 
    }
    public int getDuree() { 
    	return duree; 
    }
    public LocalDate getDateSignalement() { 
    	return dateSignalement; 
    }

    public boolean estCritique() { 
    	return priorite == Priorite.CRITIQUE; 
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Demande d && d.id == id;
    }

    @Override
    public int hashCode() { 
    	return Integer.hashCode(id); 
    }			//getId() → récupérer l'identifiant.
				//hashCode() → fournir le code de hachage pour les collections Java(utilisé par HashMap et HashSet).

    @Override
    public String toString() { 
    	return "D" + id; 
    }      
}


