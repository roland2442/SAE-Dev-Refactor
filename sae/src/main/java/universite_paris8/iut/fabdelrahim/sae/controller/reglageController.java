package universite_paris8.iut.fabdelrahim.sae.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

public class reglageController {

    @FXML
    private Slider volume;

    private Runnable actionReprendre;
    private Runnable actionRecommencer;

    // Le Controller du jeu donne les actions à faire, plus besoin de GestionJeu
    public void setActions(Runnable reprendre, Runnable recommencer) {
        this.actionReprendre = reprendre;
        this.actionRecommencer = recommencer;
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
    public void quitte(ActionEvent event) {
        GestionSon.getInstance().reprendre();
        if (actionReprendre != null) actionReprendre.run();
        fermerFenetre(event);
    }

    @FXML
    public void recommencerJeu(ActionEvent event) {
        GestionSon.getInstance().reprendre();
        if (actionRecommencer != null) actionRecommencer.run();
        fermerFenetre(event);
    }

    private void fermerFenetre(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}