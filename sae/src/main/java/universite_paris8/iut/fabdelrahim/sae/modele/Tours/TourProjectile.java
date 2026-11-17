package universite_paris8.iut.fabdelrahim.sae.modele.Tours;

import universite_paris8.iut.fabdelrahim.sae.modele.Environnement;
import universite_paris8.iut.fabdelrahim.sae.modele.Projectiles.Projectile;
import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.Enemie;

public abstract class TourProjectile extends Tour {

    // Le projectile (16 px) part du centre de la case (36 px) : (36 - 16) / 2 = 10
    private static final int DECALAGE_DEPART = 10;

    public TourProjectile(int x, int y, int portee, int degats, int vitesseTir, TypeTour type) {
        super(x, y, portee, degats, vitesseTir, type);
    }

    @Override
    protected final void gererActionTour(Enemie cible, Environnement env) {
        env.getProjectiles().add(this.creerProjectile(this.getX() + DECALAGE_DEPART, this.getY() + DECALAGE_DEPART, cible));
    }

    // Seule étape propre à chaque tour : quel projectile lancer
    protected abstract Projectile creerProjectile(double departX, double departY, Enemie cible);
}