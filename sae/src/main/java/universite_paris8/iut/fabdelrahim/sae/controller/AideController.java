package universite_paris8.iut.fabdelrahim.sae.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

public class AideController {

    private Runnable actionFermeture;

    // Le Controller du jeu donne ici l'action à faire quand la fenêtre se ferme
    public void setActionFermeture(Runnable actionFermeture) {
        this.actionFermeture = actionFermeture;
    }

    @FXML
    public void fermer(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        if (actionFermeture != null) actionFermeture.run();
    }
}