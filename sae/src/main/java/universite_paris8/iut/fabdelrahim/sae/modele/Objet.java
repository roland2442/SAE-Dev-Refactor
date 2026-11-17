package universite_paris8.iut.fabdelrahim.sae.modele;
import universite_paris8.iut.fabdelrahim.sae.modele.Tours.Tour;

public class Objet {
    public static final int DUREE_BONUS = 600; // durée de l'effet une fois ramassé
    public static final int DUREE_AU_SOL = 200; // temps avant de disparaître s'il n'est pas ramassé

    private double x;
    private double y;
    private Environnement env;
    private boolean ramasse;
    private int tempseffet;
    private int tempsRestantAuSol = DUREE_AU_SOL;
    private static int compteur = 0;
    private final String idUnique;

    public Objet(double x, double y, Environnement env){
        this.x=x;
        this.y=y;
        this.env=env;
        this.ramasse=false;
        this.tempseffet=0;
        this.idUnique = "objet_" + compteur++;
    }

    // Drop
    public static boolean lacher(){
        return Math.random()<=0.05; //5% de chance
    }

    // Utilisateur clique sur l'item
    public void ramasser(){
        if (this.ramasse) return; // un objet ne peut être ramassé qu'une fois
        this.ramasse=true;
        this.env.setTempsbonus(DUREE_BONUS);
        for (Tour tour : this.env.getTours()){
            tour.setDegats((int)(tour.getDegats() * 1.2));//+20% dégâts
        }
        this.env.objetRamasse(this); // le modèle retire l'objet du sol et suit son effet
    }

    // Un objet qui reste au sol finit par disparaître
    public void vieillir(){
        if (!this.ramasse) this.tempsRestantAuSol--;
    }

    public boolean estExpire(){
        return !this.ramasse && this.tempsRestantAuSol <= 0;
    }

    // Le temps diminue
    public void agir(){
        if (this.ramasse && this.env.getTempsbonus()>0){
            this.env.setTempsbonus(this.env.getTempsbonus() - 1);//Diminue de 1
            if (this.env.getTempsbonus()==0){ // temps fini !
                for (Tour tour: this.env.getTours()){
                    tour.setDegats((int)(tour.getDegats()/1.2)); // on enlève l'effet
                }
            }
        }
    }

    public boolean isRamasse() { return this.ramasse; }
    public double getX() { return this.x; }
    public double getY() { return this.y; }
    public String getIdUnique() { return this.idUnique; }

}