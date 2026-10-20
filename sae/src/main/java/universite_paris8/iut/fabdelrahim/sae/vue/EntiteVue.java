package universite_paris8.iut.fabdelrahim.sae.vue;

import javafx.beans.property.IntegerProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import universite_paris8.iut.fabdelrahim.sae.modele.Objet;
import universite_paris8.iut.fabdelrahim.sae.modele.Projectiles.Projectile;
import universite_paris8.iut.fabdelrahim.sae.modele.Tours.Tour;
import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.Enemie;
import java.io.IOException;
import java.net.URL;

public class EntiteVue {

    private final Pane panneauJeu;

    public EntiteVue(Pane panneauJeu) {
        this.panneauJeu = panneauJeu;
    }

    // ---------- Ajout / retrait des entités dans le panneau de jeu ----------

    public void ajouterZombie(Enemie zombie) {
        ImageView image = creerImageZombie(zombie.getIdentite(), zombie.getIdUnique());
        if (image != null) {
            image.layoutXProperty().bind(zombie.xProperty());
            image.layoutYProperty().bind(zombie.yProperty());
            panneauJeu.getChildren().add(image);
        }
    }

    public void ajouterProjectile(Projectile projectile) {
        ImageView image = creerImageProjectile(projectile.getIdentite(), projectile.getIdUnique());
        if (image != null) {
            image.layoutXProperty().bind(projectile.xProperty());
            image.layoutYProperty().bind(projectile.yProperty());
            panneauJeu.getChildren().add(image);
        }
    }

    public void ajouterTour(Tour tour) {
        if (panneauJeu.lookup("#" + tour.getIdUnique()) != null) return; // déjà affichée
        VBox image = creerImageTour(tour.getIdentite(), tour.getIdUnique(), tour.niveauProperty());
        if (image != null) {
            image.layoutXProperty().bind(tour.xProperty());
            image.layoutYProperty().bind(tour.yProperty());
            panneauJeu.getChildren().add(image);
        }
    }

    // L'objet bonus ne bouge pas. On renvoie l'image : c'est le contrôleur qui gère le clic dessus.
    public ImageView ajouterObjet(Objet objet) {
        ImageView image = new ImageView(GestionImage.getImage("Rouleau"));
        image.setFitWidth(30);
        image.setFitHeight(30);
        image.setId(objet.getIdUnique());
        image.setLayoutX(objet.getX());
        image.setLayoutY(objet.getY());
        panneauJeu.getChildren().add(image);
        return image;
    }

    // Retire l'image d'une entité (zombie, tour, projectile ou objet) à partir de son identifiant
    public void retirer(String idUnique) {
        Node image = panneauJeu.lookup("#" + idUnique);
        if (image != null) {
            panneauJeu.getChildren().remove(image);
        }
    }

    // ---------- Création des images ----------

    public ImageView creerImageZombie(String identite, String idUnique) {
        Image img = GestionImage.getImage(identite);
        if (img == null) return null;

        ImageView imageView = new ImageView(img);
        imageView.setFitWidth(36);
        imageView.setFitHeight(36);
        imageView.setId(idUnique);

        return imageView;
    }

    public ImageView creerImageProjectile(String identite, String idUnique) {
        Image img = GestionImage.getImage(identite);
        if (img == null) return null;

        ImageView imageView = new ImageView(img);
        imageView.setFitWidth(16);
        imageView.setFitHeight(16);
        imageView.setId(idUnique);

        return imageView;
    }


    public VBox creerImageTour(String identite, String idUnique, IntegerProperty niveauProperty) {
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
}