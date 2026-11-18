package universite_paris8.iut.fabdelrahim.sae.modele.Tours;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import universite_paris8.iut.fabdelrahim.sae.modele.Environnement;
import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.Enemi;
import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.ZombieNormal;
import universite_paris8.iut.fabdelrahim.sae.modele.Projectiles.Burger;

import static org.junit.jupiter.api.Assertions.*;

class TourTest {

    private Tour tourBurger;
    private Tour tourFrite;
    private Tour tourGlace;
    private Environnement env;

    @BeforeEach
    void setUp() {
        // Tour est abstraite : on teste les vraies tours du jeu
        tourBurger = new LanceBurger(0, 0);          // portée 180, dégâts 15, tir toutes les 60
        tourFrite = new MitrailletteFrite(10, 10);
        tourGlace = new BacGlace(0, 0);

        env = new Environnement();
        env.getZombies().clear();
        env.getProjectiles().clear();
    }

    @Test
    void testAmeliorer() {
        assertEquals(1, tourBurger.getNiveau());
        assertEquals(15, tourBurger.degats);
        assertEquals(60, tourBurger.vitesseTir);

        tourBurger.ameliorer();

        assertEquals(2, tourBurger.getNiveau());
        assertEquals(22, tourBurger.degats);      // 15 * 1.5
        assertEquals(45, tourBurger.vitesseTir);  // 60 * 0.75
    }

    @Test
    void testGetPrixAmelioration() {
        assertEquals(300, tourBurger.getPrixAmelioration());
        assertEquals(200, tourFrite.getPrixAmelioration());
        assertEquals(250, tourGlace.getPrixAmelioration());

        tourBurger.ameliorer();
        assertEquals(400, tourBurger.getPrixAmelioration());
    }

    @Test
    void testAttaquerCibleAPortee() {
        Enemi zombie = new ZombieNormal(20, 20);
        env.getZombies().add(zombie);

        assertEquals(0, tourBurger.cooldown);
        tourBurger.attaquer(env);

        assertEquals(tourBurger.vitesseTir, tourBurger.cooldown);
        assertEquals(1, env.getProjectiles().size());
        assertInstanceOf(Burger.class, env.getProjectiles().get(0));
    }

    @Test
    void testAttaquerCibleHorsPortee() {
        Enemi zombie = new ZombieNormal(300, 300);   // plus loin que la portée du lance-burger
        env.getZombies().add(zombie);

        tourBurger.attaquer(env);

        assertEquals(0, tourBurger.cooldown);
        assertTrue(env.getProjectiles().isEmpty());
    }

    @Test
    void testAttaquerSousCooldown() {
        Enemi zombie = new ZombieNormal(20, 20);
        env.getZombies().add(zombie);

        tourBurger.attaquer(env);
        assertEquals(tourBurger.vitesseTir, tourBurger.cooldown);
        int nbProjectilesApresTir1 = env.getProjectiles().size();

        tourBurger.attaquer(env);
        assertEquals(tourBurger.vitesseTir - 1, tourBurger.cooldown);
        assertEquals(nbProjectilesApresTir1, env.getProjectiles().size());
    }

    @Test
    void testAttaquerZombieMort() {
        Enemi zombieMort = new ZombieMort(20, 20);
        env.getZombies().add(zombieMort);

        tourBurger.attaquer(env);

        assertEquals(0, tourBurger.cooldown);
        assertTrue(env.getProjectiles().isEmpty());
    }

    @Test
    void testIdUniqueDifferent() {
        Tour autreTour = new LanceBurger(0, 0);
        assertNotEquals(tourBurger.getIdUnique(), autreTour.getIdUnique());
    }

    private static class ZombieMort extends ZombieNormal {
        public ZombieMort(int x, int y) {
            super(x, y);
        }

        @Override
        public boolean estMort() {
            return true;
        }
    }
}