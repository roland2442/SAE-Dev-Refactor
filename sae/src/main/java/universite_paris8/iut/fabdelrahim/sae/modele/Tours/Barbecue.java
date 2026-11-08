package universite_paris8.iut.fabdelrahim.sae.modele.Tours;

import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.Enemi;

import java.util.List;

public class Barbecue extends Tour {

    public Barbecue(int x, int y) {
        super(x, y, 20, 1, 12, TypeTour.BARBECUE);
    }

    @Override
    protected void appliquerEffetImmediat(Enemi cible, List<Enemi> listeZombies) {
        System.out.println("Le Barbecue brûle les zombies à proximité !");

        // brûle et blesse tous les zombies sur le barbecue
        for (Enemi z : listeZombies) {
            if (!z.estMort()) {
                // Utilisation de getX() et getY() pour extraire la valeur de la Property
                double dx = (z.getX() + 18) - (this.getX() + 18);
                double dy = (z.getY() + 18) - (this.getY() + 18);
                double distance = Math.sqrt(dx * dx + dy * dy);

                if (distance <= this.portee) {
                    z.recevoirDegats(this.degats);
                    z.bruler(); // Applique ton effet de brûlure (dégâts par seconde si tu l'as codé)
                }
            }
        }
    }
}