package universite_paris8.iut.fabdelrahim.sae.vue;

//plus besoin du import fxml
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import universite_paris8.iut.fabdelrahim.sae.modele.Chemin.Terrain;

import java.util.HashMap;
import java.util.Map;


public class TerrainVue {

    private final Terrain terrain;
    private final TilePane map;
    private final Map<Integer, Image> dictionnaireImages;

    public TerrainVue(Terrain t, TilePane map) {
        this.terrain = t;
        this.map = map;
        this.dictionnaireImages = new HashMap<>();
        this.initDictionnaire();
    }

    //methode pour se faciliter la tache et ne pas repeter le chemin
    private Image chargerImage(String nomFichier) {
        return new Image(getClass().getResourceAsStream("/universite_paris8/iut/fabdelrahim/sae/vue/" + nomFichier));
    }

    // On charge toutes nos image dans une Map
    private void initDictionnaire() {
        dictionnaireImages.put(0, chargerImage("table.png"));
        dictionnaireImages.put(1, chargerImage("tapisrose.png"));
        dictionnaireImages.put(2, chargerImage("tapisrose.png"));
        dictionnaireImages.put(3, chargerImage("entrer2.png"));
        dictionnaireImages.put(4, chargerImage("table.png"));
        dictionnaireImages.put(5, chargerImage("barrage5.png"));
        dictionnaireImages.put(6, chargerImage("barrage6.png"));
        dictionnaireImages.put(8, chargerImage("panneau8.png"));
        dictionnaireImages.put(9, chargerImage("solcasser.png"));

        dictionnaireImages.put(10, chargerImage("c1.png"));
        dictionnaireImages.put(11, chargerImage("c2.png"));
        dictionnaireImages.put(12, chargerImage("c3.png"));
        dictionnaireImages.put(13, chargerImage("c4.png"));
        dictionnaireImages.put(14, chargerImage("c5.png"));
        dictionnaireImages.put(15, chargerImage("c6.png"));
        dictionnaireImages.put(16, chargerImage("c7.png"));
        dictionnaireImages.put(17, chargerImage("c8.png"));
        dictionnaireImages.put(18, chargerImage("c9.png"));
        dictionnaireImages.put(19, chargerImage("c10.png"));
        dictionnaireImages.put(20, chargerImage("c11.png"));
        dictionnaireImages.put(21, chargerImage("c12.png"));
        dictionnaireImages.put(22, chargerImage("c13.png"));
        dictionnaireImages.put(23, chargerImage("c14.png"));
        dictionnaireImages.put(24, chargerImage("c15.png"));
        dictionnaireImages.put(25, chargerImage("c16.png"));
        dictionnaireImages.put(26, chargerImage("c17.png"));
        dictionnaireImages.put(27, chargerImage("c18.png"));

        dictionnaireImages.put(28, chargerImage("panneau9.png"));
        dictionnaireImages.put(31, chargerImage("chaise1.png"));
        dictionnaireImages.put(32, chargerImage("chaise2.png"));
        dictionnaireImages.put(33, chargerImage("chaise3.png"));
        dictionnaireImages.put(34, chargerImage("chaise4.png"));
        dictionnaireImages.put(50, chargerImage("c50.png"));
        dictionnaireImages.put(100, chargerImage("c50.png"));
    }

    public void creerTerrain() {
        // On récupère la grille via le nouveau getter
        int[][] grille = terrain.getGrille();

        int tailleTuile = 36;
        map.setPrefColumns(32);
        map.setPrefRows(21);
        map.setPrefTileWidth(tailleTuile);
        map.setPrefTileHeight(tailleTuile);

        for (int i = 0; i < grille.length; i++) {
            for (int j = 0; j < grille[i].length; j++) {
                ImageView cases = new ImageView();
                cases.setFitHeight(tailleTuile);
                cases.setFitWidth(tailleTuile);
                int numTuile = grille[i][j];
                //demande au dictionnaire l'image associée au numéro.
                Image img = dictionnaireImages.get(numTuile);
                cases.setImage(img);

                map.getChildren().add(cases);
            }
        }
    }
}
