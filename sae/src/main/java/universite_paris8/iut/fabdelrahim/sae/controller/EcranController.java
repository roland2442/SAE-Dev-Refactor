package universite_paris8.iut.fabdelrahim.sae.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import universite_paris8.iut.fabdelrahim.sae.Pizzattackapplication;
import universite_paris8.iut.fabdelrahim.sae.modele.ParametreJeu;
import universite_paris8.iut.fabdelrahim.sae.vue.GestionSon;

import java.io.IOException;

public class EcranController {

    @FXML
    private Button playBtn;



    @FXML
    private void jouer() {
        try {
            GestionSon.getInstance().demarrer("/universite_paris8/iut/fabdelrahim/sae/musique/Pizzayolo.wav");

            FXMLLoader loader = new FXMLLoader(
                    Pizzattackapplication.class.getResource("vue0.fxml")
            );
            Scene gameScene = new Scene(loader.load(), ParametreJeu.largeurEcran,ParametreJeu.hauteurEcran);
            //supression de ligne car gestion jeu a été supprimer donc plus besoin d'envoyer le controller la bas

            Stage stage = (Stage) playBtn.getScene().getWindow();
            stage.setScene(gameScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}