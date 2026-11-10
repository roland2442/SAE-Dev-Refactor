package universite_paris8.iut.fabdelrahim.sae.modele.Tours;

import universite_paris8.iut.fabdelrahim.sae.modele.Environnement;
import universite_paris8.iut.fabdelrahim.sae.modele.Projectiles.Projectile;
import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.Enemie;

public abstract class TourProjectile extends Tour {

    public TourProjectile(int x, int y, int portee, int degats, int vitesseTir, TypeTour type) {
        super(x, y, portee, degats, vitesseTir, type);
    }

    @Override
    protected final void gererActionTour(Enemie cible, Environnement env) {
        env.getProjectiles().add(this.creerProjectile(this.getX() + 10, this.getY() + 10, cible));
    }

    // Seule étape propre à chaque tour : quel projectile lancer
    protected abstract Projectile creerProjectile(double departX, double departY, Enemie cible);
}