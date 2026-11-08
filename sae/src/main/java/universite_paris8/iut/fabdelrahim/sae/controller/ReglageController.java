package universite_paris8.iut.fabdelrahim.sae.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import universite_paris8.iut.fabdelrahim.sae.vue.GestionSon;

import java.io.IOException;

public class ReglageController {

    @FXML
    private Slider volume;
    private JeuController jeuPrincipal;

    // Récuperation du contrôleur
    public void setJeuController(JeuController jeu) {
        this.jeuPrincipal = jeu;
    }

    @FXML
    public void initialize() {

        double vol = GestionSon.getInstance().getVolumeActuel();
        volume.setValue(vol);


        volume.valueProperty().addListener((obs, oldVal, newVal) -> {
            GestionSon.getInstance().setVolume(newVal.doubleValue());
        });
    }

    @FXML
    public void quitte(ActionEvent event) throws IOException {
        GestionSon.getInstance().reprendre();

        //utilisation de la référence direct
        if (this.jeuPrincipal != null) {
            this.jeuPrincipal.lancerJeu(null);
        }

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void recommencerJeu(ActionEvent event) {
        GestionSon.getInstance().reprendre();
        //même chose ici
        if (this.jeuPrincipal != null) {
            this.jeuPrincipal.recommencerJeu(null);
        }
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}