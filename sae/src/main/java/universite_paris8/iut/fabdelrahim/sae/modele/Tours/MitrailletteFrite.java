package universite_paris8.iut.fabdelrahim.sae.modele.Tours;

import universite_paris8.iut.fabdelrahim.sae.modele.Projectiles.Frites;
import universite_paris8.iut.fabdelrahim.sae.modele.Projectiles.Projectile;
import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.Enemie;

public class MitrailletteFrite extends TourProjectile {

    public MitrailletteFrite(int x, int y) {
        super(x, y, 150, 4, 4, TypeTour.MITRAILLETTE_FRITE);
    }

    @Override
    protected Projectile creerProjectile(double departX, double departY, Enemie cible) {
        Frites f = new Frites(departX, departY, cible);
        f.setDegats(this.degats);
        return f;
    }
}