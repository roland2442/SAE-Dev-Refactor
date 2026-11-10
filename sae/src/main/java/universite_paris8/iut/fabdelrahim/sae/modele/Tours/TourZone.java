package universite_paris8.iut.fabdelrahim.sae.modele.Tours;

import universite_paris8.iut.fabdelrahim.sae.modele.Environnement;
import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.Enemie;

public abstract class TourZone extends Tour {

    public TourZone(int x, int y, int portee, int degats, int vitesseTir, TypeTour type) {
        super(x, y, portee, degats, vitesseTir, type);
    }

    @Override
    protected final void gererActionTour(Enemie cible, Environnement env) {
        for (Enemie z : env.getZombies()) {
            if (!z.estMort() && this.estAPortee(z)) {
                this.appliquerSurZombie(z);
            }
        }
    }

    // Seule étape propre à chaque piège : l'effet sur un zombie à portée
    protected abstract void appliquerSurZombie(Enemie z);
}