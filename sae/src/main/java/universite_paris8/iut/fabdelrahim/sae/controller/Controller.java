package universite_paris8.iut.fabdelrahim.sae.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import universite_paris8.iut.fabdelrahim.sae.modele.Environnement;
import universite_paris8.iut.fabdelrahim.sae.modele.Objet;
import universite_paris8.iut.fabdelrahim.sae.modele.Projectiles.Projectile;
import universite_paris8.iut.fabdelrahim.sae.modele.Tours.Tour;
import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.Enemie;
import universite_paris8.iut.fabdelrahim.sae.vue.EcranVue;
import universite_paris8.iut.fabdelrahim.sae.vue.TerrainVue;
import universite_paris8.iut.fabdelrahim.sae.vue.EntiteVue;
import universite_paris8.iut.fabdelrahim.sae.vue.GestionImage;
import universite_paris8.iut.fabdelrahim.sae.modele.Tours.TypeTour;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML private TilePane map;
    @FXML private Pane panneauJeu;
    @FXML private Label labelArgent;
    @FXML private Label labelVague;
    @FXML private ProgressBar pv;

    @FXML private Label gameOverLabel;
    @FXML private Label winLabel;
    @FXML private Label vagueAnnonce;
    @FXML private Label compteARebours;

    // Bonus (Objet Rouleau)
    @FXML private ProgressBar barrebonus;
    @FXML private VBox bonuseffet;

    // Attributs Techniques
    private TerrainVue terrainVue;
    private EntiteVue entiteVue;
    private EcranVue ecranVue;
    private Timeline gameLoop;
    private Environnement env;
    private ActionJoueur action = ActionJoueur.AUCUNE;
    private TypeTour tourChoisie = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (map == null) return;

        this.env = new Environnement();
        GestionImage.loadAssets();

        this.terrainVue = new TerrainVue(env.getTerrain(), map);
        this.terrainVue.creerTerrain();

        this.panneauJeu.setPrefWidth(1152);
        this.panneauJeu.setPrefHeight(756);

        this.initJeu();
    }

    private void initJeu() {
        // La Vue reçoit les panneaux qu'elle doit gérer
        this.entiteVue = new EntiteVue(this.panneauJeu, this.map);
        this.ecranVue = new EcranVue(
                this.map,
                this.panneauJeu,
                this.labelVague,
                this.gameOverLabel,
                this.winLabel,
                this.vagueAnnonce,
                this.compteARebours
        );

        this.initEcouteurs();

        // Affichage du comptoir délégué à EntiteVue
        if (this.env.getBase() != null) {
            this.entiteVue.afficherComptoir(
                    this.env.getBase().getIdentite(),
                    this.env.getBase().getX(),
                    this.env.getBase().getY()
            );
        }

        // Bindings UI
        if (this.labelArgent != null) {
            this.labelArgent.textProperty().bind(env.argentProperty().asString(" : %d"));
        }
        if (this.labelVague != null) {
            this.labelVague.textProperty().bind(env.numeroVagueProperty().asString("Vague : %d"));
        }
        if (this.env.getBase() != null && this.pv != null) {
            this.pv.progressProperty().bind(this.env.getBase().hpProperty().divide(100.0));
        }

        // Configuration Jauge Bonus
        if (this.env != null && this.barrebonus != null && this.bonuseffet != null) {
            this.barrebonus.progressProperty().bind(this.env.tempsbonusProperty().divide(600.0));
            this.bonuseffet.setVisible(false);

            this.env.tempsbonusProperty().addListener((observable, ancienneValeur, nouvelleval) -> {
                this.bonuseffet.setVisible((int) nouvelleval > 0);
            });
        }

        this.initAnimation();
    }

    // Configuration de l'affichage dynamique : c'est le modèle qui prévient, le contrôleur ne surveille plus
    private void initEcouteurs() {
        this.initAffichageZombies();
        this.initAffichageTours();
        this.initAffichageProjectiles();
        this.initAffichageObjets();
        this.initFinDePartie();
        this.initAnnonceVague();
    }

    // Affichage dynamique des entités : le contrôleur prévient la vue, c'est elle qui affiche
    private void initAffichageZombies() {
        this.env.getZombies().addListener((ListChangeListener<Enemie>) change -> {
            while (change.next()) {
                for (Enemie zombie : change.getAddedSubList()) this.entiteVue.ajouterZombie(zombie);
                for (Enemie zombie : change.getRemoved()) this.entiteVue.retirer(zombie.getIdUnique());
            }
        });
    }

    private void initAffichageTours() {
        this.env.getTours().addListener((ListChangeListener<Tour>) change -> {
            while (change.next()) {
                for (Tour tour : change.getAddedSubList()) this.entiteVue.ajouterTour(tour);
                for (Tour tour : change.getRemoved()) this.entiteVue.retirer(tour.getIdUnique());
            }
        });
    }

    private void initAffichageProjectiles() {
        this.env.getProjectiles().addListener((ListChangeListener<Projectile>) change -> {
            while (change.next()) {
                for (Projectile p : change.getAddedSubList()) this.entiteVue.ajouterProjectile(p);
                for (Projectile p : change.getRemoved()) this.entiteVue.retirer(p.getIdUnique());
            }
        });
    }

    private void initAffichageObjets() {
        this.env.getObjets().addListener((ListChangeListener<Objet>) change -> {
            while (change.next()) {
                for (Objet objet : change.getAddedSubList()) this.faireApparaitreObjet(objet);
                for (Objet objet : change.getRemoved()) this.entiteVue.retirer(objet.getIdUnique());
            }
        });
    }

    // Le clic sur l'image du bonus appelle ramasser(), puis le modèle retire l'objet de la liste
    private void faireApparaitreObjet(Objet objet) {
        ImageView image = this.entiteVue.ajouterObjet(objet);
        image.setOnMouseClicked(event -> objet.ramasser());
    }

    // Fin de partie : le modèle expose l'état, le contrôleur réagit
    private void initFinDePartie() {
        this.env.defaiteProperty().addListener((observable, ancienne, perdu) -> {
            if (perdu) this.afficherDefaite();
        });
        this.env.victoireProperty().addListener((observable, ancienne, gagne) -> {
            if (gagne) this.afficherVictoire();
        });
    }

    // Changement de vague : annonce + décompte
    private void initAnnonceVague() {
        this.env.numeroVagueProperty().addListener((observable, ancienne, nouvelle) -> {
            if (this.gameLoop != null) this.gameLoop.pause();
            // La vue joue le décompte, puis relance la boucle de jeu via l'action donnée
            this.ecranVue.afficherAnnonceVague(nouvelle.intValue(), () -> {
                if (this.gameLoop != null) this.gameLoop.play();
            });
        });
    }

    // Boucle principale du jeu (Game Loop)
    private void initAnimation() {
        this.gameLoop = new Timeline();
        this.gameLoop.setCycleCount(Timeline.INDEFINITE);

        KeyFrame kf = new KeyFrame(
                Duration.seconds(0.017), // Environ 60 FPS
                ev -> this.env.unTourDeJeu() // Mise à jour logique du modèle, il prévient lui-même des changements
        );
        this.gameLoop.getKeyFrames().add(kf);
    }


    @FXML
    public void lancerJeu(ActionEvent event) {
        if (this.gameLoop == null || this.env.isPartieTerminee()) return; // pas de boucle, ou partie finie
        if (this.gameLoop.getStatus() != Timeline.Status.RUNNING) {
            // La boucle doit tourner AVANT de préparer la vague 1 : l'annonce de vague la met en pause
            this.gameLoop.play();
            if (this.env.getNumeroVague() == 0) {
                this.env.preparerNouvelleVague();
            }
        }
    }

    @FXML
    public void recommencerJeu(ActionEvent event) {
        if (this.gameLoop != null) {
            this.gameLoop.stop();
        }

        this.env = new Environnement();

        if (this.entiteVue != null) {
            this.entiteVue.viderPanneauJeu();
            this.ecranVue.flouter(false);
        }

        if (this.map != null) {
            this.map.getChildren().clear();
            this.terrainVue = new TerrainVue(env.getTerrain(), map);
            this.terrainVue.creerTerrain();
        }

        this.action = ActionJoueur.AUCUNE;
        this.tourChoisie = null;

        if (this.gameOverLabel != null) this.gameOverLabel.setVisible(false);
        if (this.winLabel != null) this.winLabel.setVisible(false);
        if (this.vagueAnnonce != null) this.vagueAnnonce.setVisible(false);
        if (this.compteARebours != null) this.compteARebours.setVisible(false);

        this.initJeu();
        this.lancerJeu(null);

        System.out.println("La partie a été réinitialisée avec succès !");
    }

    @FXML
    public void arreterJeu(ActionEvent event) {
        if (this.gameLoop != null) {
            this.gameLoop.pause();
        }
    }

    @FXML
    public void reglage(ActionEvent event) throws IOException {
        if (gameLoop != null) gameLoop.stop();

        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/universite_paris8/iut/fabdelrahim/sae/reglage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void aide(ActionEvent event) {
        try {
            Button boutonClique = (Button) event.getSource();

            if (boutonClique.getId() != null && boutonClique.getId().equals("nextBtn")) {
                Stage stageAide = (Stage) boutonClique.getScene().getWindow();
                if (stageAide != null) {
                    stageAide.close();
                }
                this.lancerJeu(null);
            } else {
                if (gameLoop != null) gameLoop.pause();

                Stage stage = new Stage();
                stage.setTitle("Pizzattack - L'Histoire & Règles");
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/universite_paris8/iut/fabdelrahim/sae/aide.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                stage.setScene(scene);
                stage.show();
            }
        } catch (IOException e) {
            System.err.println("Erreur de chargement de aide.fxml");
            e.printStackTrace();
        }
    }

    @FXML
    public void clicBoutonMitrailletteFrite(ActionEvent event) { choisirTour(TypeTour.MITRAILLETTE_FRITE); }

    @FXML
    public void clicBoutonLanceBurger(ActionEvent event) { choisirTour(TypeTour.LANCE_BURGER); }

    @FXML
    public void clicBoutonBacGlace(ActionEvent event) { choisirTour(TypeTour.BAC_GLACE); }

    @FXML
    public void clicBoutonBarbecue(ActionEvent event) { choisirTour(TypeTour.BARBECUE); }

    @FXML
    public void vendreT(ActionEvent event) {
        this.action = ActionJoueur.VENDRE;
        this.tourChoisie = null;
        System.out.println("Mode vente activé : cliquez sur une tour pour la revendre.");
    }

    @FXML
    public void ameliorerT(ActionEvent event) {
        this.action = ActionJoueur.AMELIORER;
        this.tourChoisie = null;
        System.out.println("Mode amélioration activé : cliquez sur une tour pour augmenter ses stats.");
    }

    private void choisirTour(TypeTour type) {
        this.action = ActionJoueur.POSER;
        this.tourChoisie = type;
    }

    // Gestion de l'action sur le terrain (Clic de souris)
    @FXML
    public void clicSurTerrain(MouseEvent event) {
        if (this.action == ActionJoueur.AUCUNE) return;

        int xAjuste = ((int) event.getX() / 36) * 36;
        int yAjuste = ((int) event.getY() / 36) * 36;

        switch (this.action) {
            case VENDRE -> this.env.vendreTour(xAjuste, yAjuste);
            case AMELIORER -> this.env.ameliorerTour(xAjuste, yAjuste);
            case POSER -> this.env.ajouterTour(xAjuste, yAjuste, this.tourChoisie);
            default -> { }
        }

        this.action = ActionJoueur.AUCUNE;
        this.tourChoisie = null;
    }

    // Affichage de la défaite (appelé quand le modèle passe defaite à vrai)
    private void afficherDefaite() {
        this.gameLoop.stop();
        this.ecranVue.afficherGameOver();
    }

    // Affichage de la victoire (appelé quand le modèle passe victoire à vrai)
    private void afficherVictoire() {
        this.gameLoop.stop();
        this.ecranVue.afficherVictoire();
    }
}
