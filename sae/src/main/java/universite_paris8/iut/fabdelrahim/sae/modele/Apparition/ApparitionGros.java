package universite_paris8.iut.fabdelrahim.sae.modele.Apparition;

import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.Enemi;
import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.ZombieGros;
import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.ZombieNormal;
import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.ZombieRapide;

// Vagues 3 et 4 : un gros zombie tous les quatre, un rapide tous les trois
public class ApparitionGros implements StrategieApparition {

    @Override
    public Enemi creerZombie(int x, int y, int zombiesRestants, double hasard) {
        if (zombiesRestants % 4 == 0) return new ZombieGros(x, y);
        if (zombiesRestants % 3 == 0) return new ZombieRapide(x, y);
        return new ZombieNormal(x, y);
    }
}
