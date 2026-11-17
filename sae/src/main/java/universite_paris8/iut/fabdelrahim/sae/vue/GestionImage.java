package universite_paris8.iut.fabdelrahim.sae.vue;

import javafx.scene.image.Image;
import universite_paris8.iut.fabdelrahim.sae.modele.Tours.TypeTour;

import java.util.HashMap;
import java.util.Map;

public class GestionImage {
    private static final Map<String, Image> sprites = new HashMap<>();

    public static void loadAssets() {
        //chargements d'images
        sprites.put("ZombieNormal", new Image(GestionImage.class.getResourceAsStream("/universite_paris8/iut/fabdelrahim/sae/vue/zombie.png")));
        sprites.put("ZombieGros", new Image(GestionImage.class.getResourceAsStream("/universite_paris8/iut/fabdelrahim/sae/vue/zombiegros.png")));
        sprites.put("ZombieRapide", new Image(GestionImage.class.getResourceAsStream("/universite_paris8/iut/fabdelrahim/sae/vue/zombierapide.png")));
        sprites.put("ZombieFamille", new Image(GestionImage.class.getResourceAsStream("/universite_paris8/iut/fabdelrahim/sae/vue/zombiefamille.png")));

        sprites.put("SuperComptoir", new Image(GestionImage.class.getResourceAsStream("/universite_paris8/iut/fabdelrahim/sae/vue/comptoir.png")));

        sprites.put("MitrailletteFrite", new Image(GestionImage.class.getResourceAsStream("/universite_paris8/iut/fabdelrahim/sae/vue/frite.png")));
        sprites.put("LanceBurger", new Image(GestionImage.class.getResourceAsStream("/universite_paris8/iut/fabdelrahim/sae/vue/burger.png")));
        sprites.put("BacGlace", new Image(GestionImage.class.getResourceAsStream("/universite_paris8/iut/fabdelrahim/sae/vue/glace.png")));
        sprites.put("Barbecue", new Image(GestionImage.class.getResourceAsStream("/universite_paris8/iut/fabdelrahim/sae/vue/soda.png")));

        sprites.put("Boss", new Image(GestionImage.class.getResourceAsStream("/universite_paris8/iut/fabdelrahim/sae/vue/boss.png")));
        sprites.put("Rouleau", new Image(GestionImage.class.getResourceAsStream("/universite_paris8/iut/fabdelrahim/sae/vue/rouleau1.png")));
        sprites.put("Frite", new Image(GestionImage.class.getResourceAsStream("/universite_paris8/iut/fabdelrahim/sae/vue/tefri.png")));
        sprites.put("Burger", new Image(GestionImage.class.getResourceAsStream("/universite_paris8/iut/fabdelrahim/sae/vue/balleb.png")));
    }

    public static Image getImage(String key) {
        return sprites.get(key);
    }

    // C'est la vue qui sait quelle image correspond à quel type de tour
    public static Image getImage(TypeTour type) {
        switch (type) {
            case LANCE_BURGER: return sprites.get("LanceBurger");
            case MITRAILLETTE_FRITE: return sprites.get("MitrailletteFrite");
            case BAC_GLACE: return sprites.get("BacGlace");
            default: return sprites.get("Barbecue");
        }
    }
}