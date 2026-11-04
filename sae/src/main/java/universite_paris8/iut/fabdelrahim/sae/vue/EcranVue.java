package universite_paris8.iut.fabdelrahim.sae.vue;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.util.Duration;

public class EcranVue {

    private TilePane map;
    private Pane panneauJeu;
    private Label labelVague;
    private Label gameOverLabel;
    private Label winLabel;
    private Label vagueAnnonce;
    private Label compteARebours;

    public EcranVue(TilePane map, Pane panneauJeu, Label labelVague, Label gameOverLabel, Label winLabel, Label vagueAnnonce, Label compteARebours) {
        this.map = map;
        this.panneauJeu = panneauJeu;
        this.labelVague = labelVague;
        this.gameOverLabel = gameOverLabel;
        this.winLabel = winLabel;
        this.vagueAnnonce = vagueAnnonce;
        this.compteARebours = compteARebours;
    }

    // Applique ou retire le flou sur la carte et le panneau de jeu
    public void flouter(boolean actif) {
        if (actif) {
            this.map.setEffect(new GaussianBlur(10));
            this.panneauJeu.setEffect(new GaussianBlur(10));
        } else {
            this.map.setEffect(null);
            this.panneauJeu.setEffect(null);
        }
    }

    // Affichage de l'écran de défaite
    public void afficherGameOver() {
        if (this.labelVague != null) {
            this.labelVague.textProperty().unbind();
            this.labelVague.setText("GAME OVER !");
        }
        if (this.gameOverLabel != null) {
            this.gameOverLabel.setVisible(true);
        }
        this.flouter(true);
    }

    // Affichage de l'écran de victoire
    public void afficherVictoire() {
        if (this.winLabel != null) {
            this.winLabel.setVisible(true);
        }
        this.flouter(true);
    }

    // Affichage de l'annonce de vague avec décompte 3-2-1
    // "actionFin" est l'action que le Controller nous passe pour relancer le jeu à la fin
    public void afficherAnnonceVague(int numeroVague, Runnable actionFin) {
        this.vagueAnnonce.setText("VAGUE " + numeroVague);
        this.vagueAnnonce.setVisible(true);
        this.compteARebours.setVisible(true);
        this.flouter(true);

        Timeline compteDown = new Timeline(
                new KeyFrame(Duration.seconds(0), e -> this.compteARebours.setText("3")),
                new KeyFrame(Duration.seconds(1), e -> this.compteARebours.setText("2")),
                new KeyFrame(Duration.seconds(2), e -> this.compteARebours.setText("1")),
                new KeyFrame(Duration.seconds(3), e -> this.compteARebours.setVisible(false))
        );

        Timeline disparition = new Timeline(
                new KeyFrame(Duration.seconds(3), e -> {
                    this.vagueAnnonce.setVisible(false);
                    this.flouter(false);

                    // Le décompte est fini : on exécute l'action donnée par le Controller !
                    if (actionFin != null) {
                        actionFin.run();
                    }
                })
        );

        compteDown.play();
        disparition.play();
    }

    // Remet tous les textes et le flou à zéro quand on recommence une partie
    public void reinitialiserEcran() {
        if (this.gameOverLabel != null) this.gameOverLabel.setVisible(false);
        if (this.winLabel != null) this.winLabel.setVisible(false);
        if (this.vagueAnnonce != null) this.vagueAnnonce.setVisible(false);
        if (this.compteARebours != null) this.compteARebours.setVisible(false);
        this.flouter(false);
    }
}