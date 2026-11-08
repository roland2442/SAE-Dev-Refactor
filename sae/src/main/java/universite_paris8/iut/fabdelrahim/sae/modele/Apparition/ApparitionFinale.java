package universite_paris8.iut.fabdelrahim.sae.modele.Apparition;

import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.Boss;
import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.Enemie;
import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.ZombieFamille;
import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.ZombieGros;
import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.ZombieNormal;
import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.ZombieRapide;

// Dernière vague : un boss en premier, puis un tirage. Si le tirage ne donne rien,
// on applique les règles des vagues 5 et plus.
public class ApparitionFinale implements StrategieApparition {

    private final StrategieApparition secours = new ApparitionAleatoire();

    @Override
    public Enemie creerZombie(int x, int y, int zombiesRestants, double hasard) {
        if (zombiesRestants == 25) return new Boss(x, y);
        if (hasard < 0.20) return new ZombieGros(x, y);
        if (hasard < 0.40) return new ZombieFamille(x, y);
        if (hasard < 0.50) return new ZombieRapide(x, y);
        if (hasard < 0.55) return new ZombieNormal(x, y);
        return secours.creerZombie(x, y, zombiesRestants, hasard);
    }
}
