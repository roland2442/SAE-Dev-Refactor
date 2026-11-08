package universite_paris8.iut.fabdelrahim.sae.modele.Tours;

import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.Enemi;

import java.util.List;

public class BacGlace extends Tour {

    public BacGlace(int x, int y) {
        super(x, y, 50, 0, 12, TypeTour.BAC_GLACE);
    }

    @Override
    protected void appliquerEffetImmediat(Enemi cible, List<Enemi> listeZombies) {
        System.out.println("Le Bac à Glace s'active et gèle la zone !");

        // on parcourt tous les zombies pour ralentir ceux à portée
        for (Enemi z : listeZombies) {
            if (!z.estMort()) {
                //Utilisation de getX() et getY() pour extraire la valeur de la Property
                double dx = (z.getX() + 18) - (this.getX() + 18);
                double dy = (z.getY() + 18) - (this.getY() + 18);
                double distance = Math.sqrt(dx * dx + dy * dy);

                if (distance <= this.portee) {
                    z.ralentissement(); // Applique ton effet de gel/ralentissement
                }
            }
        }
    }
}