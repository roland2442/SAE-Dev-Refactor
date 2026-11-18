package universite_paris8.iut.fabdelrahim.sae.modele;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnvironnementTest {

    // Le modèle ne fait un vrai tour de jeu qu'une frame sur trois : on avance donc par groupes de trois
    private void avancer(Environnement env, int toursLogiques) {
        for (int i = 0; i < toursLogiques * 3; i++) {
            env.unTourDeJeu();
        }
    }

    @Test
    void laPartieEstPerdueQuandLeComptoirEstDetruit() {
        Environnement env = new Environnement();
        env.getBase().recevoirDegats(Comptoir.HP_MAX);

        avancer(env, 1);

        assertTrue(env.defaiteProperty().get());
        assertTrue(env.isPartieTerminee());
    }

    @Test
    void unObjetRamasseQuitteLeSolEtLanceLeBonus() {
        Environnement env = new Environnement();
        Objet objet = new Objet(100, 100, env);
        env.getObjets().add(objet);

        objet.ramasser();

        assertTrue(env.getObjets().isEmpty());
        assertEquals(Objet.DUREE_BONUS, env.getTempsbonus());
    }

    @Test
    void unObjetNonRamasseDisparaitAuBoutDUnMoment() {
        Environnement env = new Environnement();
        env.getObjets().add(new Objet(100, 100, env));

        avancer(env, Objet.DUREE_AU_SOL);

        assertTrue(env.getObjets().isEmpty());
    }
}
