package universite_paris8.iut.fabdelrahim.sae.modele.Apparition;

import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.Enemi;
import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.ZombieFamille;
import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.ZombieGros;
import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.ZombieNormal;
import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.ZombieRapide;

// Vagues 5 et plus : le type de zombie est tiré au hasard
public class ApparitionAleatoire implements StrategieApparition {

    @Override
    public Enemi creerZombie(int x, int y, int zombiesRestants, double hasard) {
        if (hasard < 0.20) return new ZombieGros(x, y);
        if (hasard < 0.40) return new ZombieFamille(x, y);
        if (hasard < 0.70) return new ZombieRapide(x, y);
        return new ZombieNormal(x, y);
    }
}
