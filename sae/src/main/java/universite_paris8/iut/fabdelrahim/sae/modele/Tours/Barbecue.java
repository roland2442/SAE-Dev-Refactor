package universite_paris8.iut.fabdelrahim.sae.modele.Tours;

import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.Enemi;

public class Barbecue extends TourZone {

    public Barbecue(int x, int y) {
        super(x, y, 20, 1, 12, TypeTour.BARBECUE);
    }

    @Override
    protected void appliquerSurZombie(Enemi z) {
        z.recevoirDegats(this.degats);
        z.bruler();
    }
}