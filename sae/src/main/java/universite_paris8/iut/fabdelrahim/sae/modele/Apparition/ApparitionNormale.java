package universite_paris8.iut.fabdelrahim.sae.modele.Apparition;

import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.Enemi;
import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.ZombieNormal;

// Vague 1 : uniquement des zombies normaux
public class ApparitionNormale implements StrategieApparition {

    @Override
    public Enemi creerZombie(int x, int y, int zombiesRestants, double hasard) {
        return new ZombieNormal(x, y);
    }
}
