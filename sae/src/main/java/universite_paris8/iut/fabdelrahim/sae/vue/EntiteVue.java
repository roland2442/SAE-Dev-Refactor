package universite_paris8.iut.fabdelrahim.sae.vue;

import javafx.beans.property.IntegerProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.net.URL;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;

public class EntiteVue {

    private Pane panneauJeu;
    private TilePane map;

    public EntiteVue(Pane panneauJeu, TilePane map) {
        this.panneauJeu = panneauJeu;
        this.map = map;
    }

    public ImageView creerImageZombie(String identite, String idUnique) {
        Image img = GestionImage.getImage(identite);
        if (img == null) return null;

        ImageView imageView = new ImageView(img);
        imageView.setFitWidth(36);
        imageView.setFitHeight(36);
        imageView.setId(idUnique);

        this.panneauJeu.getChildren().add(imageView);
        return imageView;
    }

    public ImageView creerImageProjectile(String identite, String idUnique) {
        Image img = GestionImage.getImage(identite);
        if (img == null) return null;

        ImageView imageView = new ImageView(img);
        imageView.setFitWidth(16);
        imageView.setFitHeight(16);
        imageView.setId(idUnique);

        this.panneauJeu.getChildren().add(imageView);
        return imageView;

    }


    public VBox creerImageTour(String identite, String idUnique, IntegerProperty niveauProperty) {
        if (this.panneauJeu.lookup("#" + idUnique) != null) return null;

        try {
            URL fxmlUrl = getClass().getResource("/universite_paris8/iut/fabdelrahim/sae/barreniv.fxml");
            if (fxmlUrl == null) return null;

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            VBox conteneurTour = loader.load();

            Label labelNiveau = (Label) loader.getNamespace().get("labelNiveau");
            ImageView imageTour = (ImageView) loader.getNamespace().get("imageTour");

            Image img = GestionImage.getImage(identite);
            if (img != null && imageTour != null) {
                imageTour.setImage(img);
            }

            conteneurTour.setId(idUnique);

            if (labelNiveau != null && niveauProperty != null) {
                labelNiveau.textProperty().bind(niveauProperty.asString("Nv. %d"));
            }

            this.panneauJeu.getChildren().add(conteneurTour);
            return conteneurTour;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public ImageView creerImageComptoir(String identite, int x, int y) {
        Image img = GestionImage.getImage(identite);
        if (img == null) return null;

        ImageView imageComptoir = new ImageView(img);
        imageComptoir.setFitWidth(36);
        imageComptoir.setFitHeight(36);
        imageComptoir.setLayoutX(x);
        imageComptoir.setLayoutY(y);

        return imageComptoir;
    }
    public ImageView creerImageObjet(double x, double y) {
        Image img = GestionImage.getImage("Rouleau");
        if (img == null) return null;

        ImageView vueObjet = new ImageView(img);
        vueObjet.setFitWidth(30);
        vueObjet.setFitHeight(30);
        vueObjet.setTranslateX(x);
        vueObjet.setTranslateY(y);

        this.panneauJeu.getChildren().add(vueObjet);
        return vueObjet;
    }
    public void afficherComptoir(String identite, int x, int y) {
        Image img = GestionImage.getImage(identite);
        if (img == null) return;

        ImageView imageComptoir = new ImageView(img);
        imageComptoir.setFitWidth(36);
        imageComptoir.setFitHeight(36);
        imageComptoir.setLayoutX(x);
        imageComptoir.setLayoutY(y);

        this.panneauJeu.getChildren().add(imageComptoir);
    }
    // Suppression générique par ID (Zombies, Tours, Projectiles)
    public void supprimerEntiteParId(String idUnique) {
        Node imgView = this.panneauJeu.lookup("#" + idUnique);
        if (imgView != null) {
            this.panneauJeu.getChildren().remove(imgView);
        }
    }

    // Suppression directe d'un Node (utilisé quand on clique sur le Rouleau)
    public void supprimerNode(Node node) {
        this.panneauJeu.getChildren().remove(node);
    }

    public void viderPanneauJeu() {
        if (this.panneauJeu != null) {
            this.panneauJeu.getChildren().clear();
        }
    }

}
