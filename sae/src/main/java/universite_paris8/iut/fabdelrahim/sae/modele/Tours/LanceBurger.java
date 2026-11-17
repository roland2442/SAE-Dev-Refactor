package universite_paris8.iut.fabdelrahim.sae.modele.Tours;

import universite_paris8.iut.fabdelrahim.sae.modele.Projectiles.Burger;
import universite_paris8.iut.fabdelrahim.sae.modele.Projectiles.Projectile;
import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.Enemie;

public class LanceBurger extends TourProjectile {

    public LanceBurger(int x, int y) {
        super(x, y, 180, 15, 60, TypeTour.LANCE_BURGER);
    }

    @Override
    protected Projectile creerProjectile(double departX, double departY, Enemie cible) {
        Burger b = new Burger(departX, departY, cible);
        b.setDegats(this.degats);
        return b;
    }
}