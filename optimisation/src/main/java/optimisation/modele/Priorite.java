package optimisation.modele;

public enum Priorite {
    CRITIQUE(8, 1),
    HAUTE(4, 3),
    NORMALE(2, 7),
    BASSE(1, 14);

    private final int poids;
    private final int delaiMaxJours;

    Priorite(int poids, int delaiMaxJours) {
        this.poids = poids;
        this.delaiMaxJours = delaiMaxJours;
    }

    public int getPoids() { 
    	return poids;
    }
    public int getDelaiMaxJours() {
    	return delaiMaxJours; 
    }
}
