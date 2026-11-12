package universite_paris8.iut.fabdelrahim.sae.modele.Tours;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import universite_paris8.iut.fabdelrahim.sae.modele.Environnement;
import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.Enemie;

import java.util.List;

public abstract class Tour {
    protected IntegerProperty x;
    protected IntegerProperty y;
    protected int portee;
    protected int degats;
    protected int cooldown;
    protected int vitesseTir;
    protected TypeTour type;
    protected IntegerProperty niveau;

    private static int compteur = 0;
    private String idUnique;

    public Tour(int x, int y, int portee, int degats, int vitesseTir, TypeTour type) {
        this.x = new SimpleIntegerProperty(x);
        this.y = new SimpleIntegerProperty(y);
        this.portee = portee;
        this.degats = degats;
        this.cooldown = 0;
        this.vitesseTir = vitesseTir;
        this.type = type;
        this.idUnique = "tour_" + compteur++;
        this.niveau = new SimpleIntegerProperty(1);
    }

    public void ameliorer() {
        this.niveau.set(this.getNiveau() + 1);
        this.degats = (int) (this.degats * 1.5);
        this.portee = (int) (this.portee * 1.15);
        this.vitesseTir = (int) (this.vitesseTir * 0.75);
    }

    public int getPrixAmelioration() {
        // A chaque amélioration, le prix augmente de 100
        return this.type.getPrix() + 100 * getNiveau();
    }

    // MÉTHODE PATRON : l'ordre des étapes est fixe, les sous-classes ne peuvent pas le changer
    public final void attaquer(Environnement env) {
        if (this.cooldown > 0) {
            this.cooldown--;
            return;
        }

        Enemie cible = this.trouverCible(env.getZombies());
        if (cible != null) {
            this.gererActionTour(cible, env);
            this.cooldown = this.vitesseTir;
        }
    }

    // Étape commune : premier zombie vivant à portée
    protected Enemie trouverCible(List<Enemie> zombies) {
        for (Enemie z : zombies) {
            if (!z.estMort() && this.estAPortee(z)) {
                return z;
            }
        }
        return null;
    }

    // Étape commune : distance entre le centre de la tour (36x36) et celui du zombie (36x36)
    protected boolean estAPortee(Enemie z) {
        double dx = (this.getX() + 18) - (z.getX() + 18);
        double dy = (this.getY() + 18) - (z.getY() + 18);
        return Math.sqrt(dx * dx + dy * dy) <= this.portee;
    }

    // Étape qui change selon la tour : chaque sous-classe doit la définir
    protected abstract void gererActionTour(Enemie cible, Environnement env);

    // Getters et Setters
    public int getX() { return this.x.get(); }
    public int getY() { return this.y.get(); }
    public void setX(int x) { this.x.set(x); }
    public void setY(int y) { this.y.set(y); }
    public IntegerProperty xProperty() { return this.x; }
    public IntegerProperty yProperty() { return this.y; }

    public String getIdUnique() { return this.idUnique; }
    public TypeTour getType() { return this.type; }

    public int getNiveau() { return this.niveau.get(); }
    public IntegerProperty niveauProperty() { return this.niveau; }

    public int getDegats() { return this.degats; }
    public void setDegats(int degats) { this.degats = degats; }
}