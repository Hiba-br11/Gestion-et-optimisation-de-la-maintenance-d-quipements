package optimisation.modele;

public record Affectation(Demande demande, Technicien technicien) {
    @Override
    public String toString() { 
    	return demande + " -> " + technicien; 
    }
}
