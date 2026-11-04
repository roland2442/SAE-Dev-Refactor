package universite_paris8.iut.fabdelrahim.sae.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
    private String tourAcheteeEnCours = null;
    private int derniereVagueAffichee = 0;

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

    private void initEcouteurs() {
        // Zombies
        this.env.getZombies().addListener((ListChangeListener<Enemie>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (Enemie nouveauZombie : change.getAddedSubList()) {
                        ImageView iv = this.entiteVue.creerImageZombie(nouveauZombie.getIdentite(), nouveauZombie.getIdUnique());
                        if (iv != null) {
                            iv.layoutXProperty().bind(nouveauZombie.xProperty());
                            iv.layoutYProperty().bind(nouveauZombie.yProperty());
                        }
                    }
                }
                if (change.wasRemoved()) {
                    for (Enemie zombieMort : change.getRemoved()) {
                        this.entiteVue.supprimerEntiteParId(zombieMort.getIdUnique());
                    }
                }
            }
        });

        // Tours
        this.env.getTours().addListener((ListChangeListener<Tour>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (Tour nouvelleTour : change.getAddedSubList()) {
                        Node iv = this.entiteVue.creerImageTour(
                                nouvelleTour.getIdentite(),
                                nouvelleTour.getIdUnique(),
                                nouvelleTour.niveauProperty()
                        );
                        if (iv != null) {
                            iv.layoutXProperty().bind(nouvelleTour.xProperty());
                            iv.layoutYProperty().bind(nouvelleTour.yProperty());
                        }
                    }
                }
                if (change.wasRemoved()) {
                    for (Tour tourRetiree : change.getRemoved()) {
                        this.entiteVue.supprimerEntiteParId(tourRetiree.getIdUnique());
                    }
                }
            }
        });

        // Projectiles
        this.env.getProjectiles().addListener((ListChangeListener<Projectile>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (Projectile nouveauProj : change.getAddedSubList()) {
                        ImageView iv = this.entiteVue.creerImageProjectile(nouveauProj.getIdentite(), nouveauProj.getIdUnique());
                        if (iv != null) {
                            iv.layoutXProperty().bind(nouveauProj.xProperty());
                            iv.layoutYProperty().bind(nouveauProj.yProperty());
                        }
                    }
                }
                if (change.wasRemoved()) {
                    for (Projectile projDetruit : change.getRemoved()) {
                        this.entiteVue.supprimerEntiteParId(projDetruit.getIdUnique());
                    }
                }
            }
        });
    }

    private void initAnimation() {
        this.gameLoop = new Timeline();
        this.gameLoop.setCycleCount(Timeline.INDEFINITE);

        KeyFrame kf = new KeyFrame(
                Duration.seconds(0.017),
                ev -> {
                    this.env.unTourDeJeu();

                    // Gestion de l'Objet Bonus (Rouleau)
                    Objet obj = this.env.getObjdrop();
                    if (obj != null && !obj.isDejaAffiche() && !obj.isRamasse()) {
                        faireApparaitreObjet(obj);
                        obj.setDejaAffiche(true);
                    }

                    // Condition de Défaite
                    if (this.env.getBase() != null && this.env.getBase().estDetruit()) {
                        this.gameLoop.stop();
                        this.labelVague.textProperty().unbind();
                        this.labelVague.setText("GAME OVER !");
                        if (this.gameOverLabel != null) this.gameOverLabel.setVisible(true);
                        this.ecranVue.flouter(true);
                    }

                    // Condition de Victoire
                    if (this.env.toutesVaguesTerminees()) {
                        this.gameLoop.stop();
                        if (this.winLabel != null) this.winLabel.setVisible(true);
                        this.ecranVue.flouter(true);
                    }

                    // Changement de vague (Annonce + Décompte)
                    if (this.env.getNumeroVague() != derniereVagueAffichee) {
                        derniereVagueAffichee = this.env.getNumeroVague();
                        this.gameLoop.pause();
                        this.ecranVue.afficherAnnonceVague(
                                derniereVagueAffichee,
                                () -> {
                                    if (this.gameLoop != null) this.gameLoop.play();
                                }
                        );
                    }
                }
        );
        this.gameLoop.getKeyFrames().add(kf);
    }

    // Le Controller délègue la création de l'image à EntiteVue et ne garde que la réaction au clic !
    public void faireApparaitreObjet(Objet objet) {
        ImageView vueObjet = this.entiteVue.creerImageObjet(objet.getX(), objet.getY());
        if (vueObjet != null) {
            vueObjet.setOnMouseClicked(event -> {
                objet.ramasser(); // Action logique sur le modèle
                this.entiteVue.supprimerNode(vueObjet); // Demande à la vue de retirer l'image
            });
        }
    }

    @FXML
    public void lancerJeu(ActionEvent event) {
        if (this.gameLoop != null && this.gameLoop.getStatus() != Timeline.Status.RUNNING) {
            if (this.env.getNumeroVague() == 0) {
                this.env.preparerNouvelleVague();
            }
            this.gameLoop.play();
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

        this.tourAcheteeEnCours = null;
        this.derniereVagueAffichee = 0;

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
    public void clicBoutonMitrailletteFrite(ActionEvent event) {
        this.tourAcheteeEnCours = "MitrailletteFrite";
    }

    @FXML
    public void clicBoutonLanceBurger(ActionEvent event) {
        this.tourAcheteeEnCours = "LanceBurger";
    }

    @FXML
    public void clicBoutonBacGlace(ActionEvent event) {
        this.tourAcheteeEnCours = "BacGlace";
    }

    @FXML
    public void clicBoutonBarbecue(ActionEvent event) {
        this.tourAcheteeEnCours = "Barbecue";
    }

    @FXML
    public void vendreT(ActionEvent event) {
        this.tourAcheteeEnCours = "VENDRE";
        System.out.println("Mode vente activé : cliquez sur une tour pour la revendre.");
    }

    @FXML
    public void ameliorerT(ActionEvent event) {
        this.tourAcheteeEnCours = "AMELIORER";
        System.out.println("Mode amélioration activé : cliquez sur une tour pour augmenter ses stats.");
    }

    @FXML
    public void clicSurTerrain(MouseEvent event) {
        if (this.tourAcheteeEnCours == null) return;

        int xAjuste = ((int) event.getX() / 36) * 36;
        int yAjuste = ((int) event.getY() / 36) * 36;

        if (this.tourAcheteeEnCours.equals("VENDRE")) {
            this.env.vendreTour(xAjuste, yAjuste);
        } else if (this.tourAcheteeEnCours.equals("AMELIORER")) {
            this.env.ameliorerTour(xAjuste, yAjuste);
        } else {
            this.env.ajouterTour(xAjuste, yAjuste, this.tourAcheteeEnCours);
        }

        this.tourAcheteeEnCours = null;
    }
}
