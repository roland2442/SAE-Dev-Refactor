package universite_paris8.iut.fabdelrahim.sae.modele.Tours;

public enum TypeTour {
    //prix  sur chemin ?
    LANCE_BURGER(200, false),
    MITRAILLETTE_FRITE(100, false),
    BAC_GLACE(150, true),
    BARBECUE(250, true);

    private final int prix;
    private final boolean surChemin;

    TypeTour(int prix, boolean surChemin) {
        this.prix = prix;
        this.surChemin = surChemin;
    }

    public int getPrix() { return prix; }
    public boolean estPoseSurChemin() { return surChemin; }
}