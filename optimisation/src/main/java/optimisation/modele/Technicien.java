package optimisation.modele;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Technicien {
    public static final int CAPACITE_JOURNALIERE = 8;

    private final int id;
    private final String prenom;
    private final Set<Categorie> competences;
    private final Set<LocalDate> indisponibilites;

    public Technicien(int id, String prenom, Set<Categorie> competences,Set<LocalDate> indisponibilites) {
        this.id = id;
        this.prenom = prenom;
        this.competences = new HashSet<>(competences);
        this.indisponibilites = new HashSet<>(indisponibilites);
    }
    
    public int getId() { 
    	return id; 
    }     
    public String getPrenom() {
    	return prenom; 
    }
    public Set<Categorie> getCompetences() {
    	return competences; 
    }
    public Set<LocalDate> getIndisponibilites() {
    	return indisponibilites; 
    }

    public boolean aCompetence(Categorie c) { 
    	return competences.contains(c); 
    }

    public boolean estDisponible(LocalDate jour) { 
    	return !indisponibilites.contains(jour); 
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Technicien t && t.id == id;
    }

    @Override
    public int hashCode() { 
    	return Integer.hashCode(id); 
    }

    @Override
    public String toString() { 
    	return prenom; 
    }
}