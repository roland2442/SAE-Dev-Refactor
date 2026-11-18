package universite_paris8.iut.fabdelrahim.sae.modele.Apparition;

import org.junit.jupiter.api.Test;
import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.Boss;
import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.ZombieNormal;
import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.ZombieRapide;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ApparitionTest {

    @Test
    void laVague1NeFaitApparaitreQueDesZombiesNormaux() {
        assertTrue(new ApparitionNormale().creerZombie(0, 0, 5, 0.5) instanceof ZombieNormal);
    }

    @Test
    void laVague2FaitApparaitreUnRapideTousLesTroisZombies() {
        assertTrue(new ApparitionRapide().creerZombie(0, 0, 3, 0.5) instanceof ZombieRapide);
        assertTrue(new ApparitionRapide().creerZombie(0, 0, 4, 0.5) instanceof ZombieNormal);
    }

    @Test
    void laDerniereVagueCommenceParUnBoss() {
        assertTrue(new ApparitionFinale().creerZombie(0, 0, 25, 0.9) instanceof Boss);
    }

    @Test
    void laDerniereVagueRetombeSurLesReglesDesVagues5EtPlus() {
        // tirage de 0.6 : aucun cas de la dernière vague, la stratégie de secours donne un zombie rapide
        assertTrue(new ApparitionFinale().creerZombie(0, 0, 10, 0.6) instanceof ZombieRapide);
    }
}
