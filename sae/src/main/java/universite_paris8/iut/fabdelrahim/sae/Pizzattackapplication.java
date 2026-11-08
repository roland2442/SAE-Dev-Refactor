package universite_paris8.iut.fabdelrahim.sae;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import universite_paris8.iut.fabdelrahim.sae.modele.ParametreJeu;

import java.io.IOException;

public class Pizzattackapplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                Pizzattackapplication.class.getResource("ecran.fxml")
        );
        Scene scene = new Scene(fxmlLoader.load(), ParametreJeu.largeurEcran,ParametreJeu.hauteurEcran);
        stage.setTitle("pizzattack!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}