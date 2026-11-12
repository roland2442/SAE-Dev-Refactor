package universite_paris8.iut.fabdelrahim.sae.modele.Tours;

import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.Enemie;

public class BacGlace extends TourZone {

    public BacGlace(int x, int y) {
        super(x, y, 50, 0, 12, TypeTour.BAC_GLACE);
    }

    @Override
    protected void appliquerSurZombie(Enemie z) {
        z.ralentissement();
    }
}