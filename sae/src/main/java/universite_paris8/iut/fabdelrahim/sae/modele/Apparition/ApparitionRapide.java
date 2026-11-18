package universite_paris8.iut.fabdelrahim.sae.modele.Apparition;

import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.Enemi;
import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.ZombieNormal;
import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.ZombieRapide;

// Vague 2 : un zombie rapide tous les trois zombies
public class ApparitionRapide implements StrategieApparition {

    @Override
    public Enemi creerZombie(int x, int y, int zombiesRestants, double hasard) {
        if (zombiesRestants % 3 == 0) return new ZombieRapide(x, y);
        return new ZombieNormal(x, y);
    }
}
