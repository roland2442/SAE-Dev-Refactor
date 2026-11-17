package universite_paris8.iut.fabdelrahim.sae.modele.Apparition;

import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.Enemie;

/**
 * Une façon de choisir le zombie qui apparaît. Chaque palier de difficulté a sa propre stratégie.
 * hasard est un nombre tiré entre 0 et 1, une seule fois par apparition.
 */
public interface StrategieApparition {

    Enemie creerZombie(int x, int y, int zombiesRestants, double hasard);
}
