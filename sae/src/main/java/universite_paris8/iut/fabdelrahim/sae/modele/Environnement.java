package universite_paris8.iut.fabdelrahim.sae.modele;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import universite_paris8.iut.fabdelrahim.sae.modele.Apparition.*;
import universite_paris8.iut.fabdelrahim.sae.modele.Chemin.Bfs;
import universite_paris8.iut.fabdelrahim.sae.modele.Chemin.Point;
import universite_paris8.iut.fabdelrahim.sae.modele.Chemin.Terrain;
import universite_paris8.iut.fabdelrahim.sae.modele.Projectiles.Projectile;
import universite_paris8.iut.fabdelrahim.sae.modele.Tours.*;
import universite_paris8.iut.fabdelrahim.sae.modele.Zombies.*;

public class Environnement {

    private static final int ArgentDepart = 1000000;
    private static final int RecompenseParZombie = 10;


    private static final int Delaiavantaparition = 10;
    private static final int DelaientreVague = 60;
    private static final int PAS_LOGIQUE = 3;
    private static final int NombreVaguesMax = 10;

    public static final int TailleCase = 36; // taille d'une case en pixels, aussi utilisée par la vue
    private static final int MAX_TOURS_SUR_CHEMIN = 6;   // nombre maximum de pièges sur le chemin
    private static final int CASE_CONSTRUCTIBLE = 0;     // les tables où on peu poser les tours
    private static final int CASE_CHEMIN = 1;            // le chemin des zombies
    private static final int CASE_CHEMIN_FIN = 100;      // 2 cases du chemin juste avant la sortie

    private final Terrain terrain;
    private final List<Point> chemin;
    private final Comptoir base;
    private final ObservableList<Tour> tours;
    private final ObservableList<Enemie> zombies;
    private final ObservableList<Projectile> projectiles;
    private final ObservableList<Objet> objets; // objets bonus posés au sol

    private final IntegerProperty argent;
    private final IntegerProperty numeroVague;
    private final BooleanProperty vagueEnCours;
    private final BooleanProperty defaite;
    private final BooleanProperty victoire;

    private final IntegerProperty tempsbonus;

    private int temps;
    private int zombiesRestantsASpawner;
    private StrategieApparition strategieApparition = new ApparitionNormale();
    private int delaiAvantProchainZombie;
    private int tempsAvantProchaineVague;
    private int compteurToursSurChemin = 0;

    private Objet bonusActif = null; // objet ramassé dont l'effet est en cours

    public Environnement() {
        this.terrain = new Terrain();
        this.tours = FXCollections.observableArrayList();
        this.zombies = FXCollections.observableArrayList();
        this.projectiles = FXCollections.observableArrayList();
        this.objets = FXCollections.observableArrayList();

        this.argent = new SimpleIntegerProperty(ArgentDepart);
        this.numeroVague = new SimpleIntegerProperty(0);
        this.vagueEnCours = new SimpleBooleanProperty(false);
        this.defaite = new SimpleBooleanProperty(false);
        this.victoire = new SimpleBooleanProperty(false);
        this.tempsbonus = new SimpleIntegerProperty(0);
        this.temps = 0;

        Point debut = terrain.trouverEntree();
        Point arrivee = terrain.trouverSortie();
        this.chemin = Bfs.bfs(terrain.getGrille(), debut, arrivee);

        if (chemin.isEmpty()) {
            System.err.println("Aucun chemin valide trouvé !");
        }

        this.base = new Comptoir(
                (int) (arrivee.y * TailleCase),
                (int) (arrivee.x * TailleCase),
                "SuperComptoir"
        );
    }

    public void preparerNouvelleVague() {
        numeroVague.set(getNumeroVague() + 1);
        strategieApparition = choisirStrategie(getNumeroVague());
        vagueEnCours.set(true);
        if (getNumeroVague() < NombreVaguesMax) {
            zombiesRestantsASpawner = 10 * getNumeroVague();
            delaiAvantProchainZombie = 0;
        } else {
            zombiesRestantsASpawner = 10 * (getNumeroVague() / 2);
        }
    }

    // Ramène un pixel au coin de la case qui le contient
    private int alignerSurCase(int pixel) {
        return (pixel / TailleCase) * TailleCase;
    }

    // Numéro de la case de la grille qui contient ce point (en pixels)
    private int idTuileEn(int pixelX, int pixelY) {
        return terrain.getCase(pixelY / TailleCase, pixelX / TailleCase);
    }

    // Vrai si ce numéro de case fait partie du chemin des zombies
    private boolean estSurChemin(int idTuile) {
        return idTuile == CASE_CHEMIN || idTuile == CASE_CHEMIN_FIN;
    }

    // Renvoie la tour posée sur la case qui contient ce point ou null s'il n'y en a pas
    private Tour trouverTourEn(int pixelX, int pixelY) {
        int x = alignerSurCase(pixelX);
        int y = alignerSurCase(pixelY);
        for (Tour t : tours) {
            if (t.getX() == x && t.getY() == y) {
                return t;
            }
        }
        return null;
    }

    public void ajouterTour(int pixelX, int pixelY, TypeTour type) {
        pixelX = alignerSurCase(pixelX);
        pixelY = alignerSurCase(pixelY);

        if (trouverTourEn(pixelX, pixelY) != null) return;   // case déjà occupée

        int idTuile = idTuileEn(pixelX, pixelY);
        boolean surChemin = estSurChemin(idTuile);

        if (type.estPoseSurChemin()) {
            // Les tours de zone se placent sur le chemin, avec une limite
            if (surChemin) {
                if (compteurToursSurChemin >= MAX_TOURS_SUR_CHEMIN) return;
            } else if (idTuile != CASE_CONSTRUCTIBLE) {
                return;
            }
        } else if (idTuile != CASE_CONSTRUCTIBLE) {
            return;                                          // les autres tours seulement sur une table
        }

        if (!payerAchat(type.getPrix())) return;

        tours.add(creerTour(pixelX, pixelY, type));
        if (surChemin) {
            compteurToursSurChemin++;
        }
    }



    private Tour creerTour(int x, int y, TypeTour type) {
        return switch (type) {
            case LANCE_BURGER -> new LanceBurger(x, y);
            case MITRAILLETTE_FRITE -> new MitrailletteFrite(x, y);
            case BAC_GLACE -> new BacGlace(x, y);
            case BARBECUE -> new Barbecue(x, y);
        };
    }

    public void unTourDeJeu() {
        temps++;
        // Permet d'alléger les calculs en limitant la mise à jour logique à une frame sur trois
        if (temps % PAS_LOGIQUE != 0) return;

        gererAparition();
        faireAttaquerLesTours();
        mettreAJourProjectiles();
        mettreAJourZombies();
        gererTransitionVague();
        gererObjetsAuSol();
        gererBonusActif();
        verifierFinDePartie();
    }

    // Un objet qui n'est pas ramassé disparaît au bout d'un moment : un nouveau bonus pourra tomber
    private void gererObjetsAuSol() {
        for (Objet objet : new ArrayList<>(objets)) {
            objet.vieillir();
            if (objet.estExpire()) {
                objets.remove(objet);
            }
        }
    }

    // L'effet du bonus ramassé diminue à chaque tour, puis un nouveau bonus pourra tomber
    private void gererBonusActif() {
        if (bonusActif == null) return;

        bonusActif.agir();
        if (getTempsbonus() == 0) {
            bonusActif = null;
        }
    }

    // Le modèle décide lui-même si la partie est finie, le contrôleur est prévenu par les propriétés
    private void verifierFinDePartie() {
        if (isPartieTerminee()) return;

        if (base != null && base.estDetruit()) {
            defaite.set(true);
        } else if (toutesVaguesTerminees()) {
            victoire.set(true);
        }
    }

    // Appelé par Objet quand le joueur le ramasse : il quitte le sol mais son effet continue
    public void objetRamasse(Objet objet) {
        objets.remove(objet);
        bonusActif = objet;
    }

    private void gererAparition() {
        if (zombiesRestantsASpawner <= 0) return;

        delaiAvantProchainZombie--;
        if (delaiAvantProchainZombie <= 0) {
            faireApparaitreZombie();
            zombiesRestantsASpawner--;
            delaiAvantProchainZombie = Delaiavantaparition + 8;
        }
    }

    private void faireAttaquerLesTours() {
        for (Tour t : tours) {
            t.attaquer(this);
        }
    }

    private void mettreAJourProjectiles() {
        // Parcours inversé indispensable pour éviter les erreurs de modification de liste pendant la suppression
        for (int i = projectiles.size() - 1; i >= 0; i--) {
            Projectile p = projectiles.get(i);
            p.avancer();

            if (p.isATouche() || p.getCible() == null || p.getCible().estMort()) {
                if (p.isATouche()) {
                    p.appliquerEffet(this);
                }
                projectiles.remove(i);
            }
        }
    }

    private void mettreAJourZombies() {
        // Boucle à rebours pour nettoyer la liste et injecter des enfants à la volée sans casser l'index
        for (int i = zombies.size() - 1; i >= 0; i--) {
            Enemie z = zombies.get(i);
            z.avancer();

            if (z.estArrive() && base != null) {
                base.recevoirDegats(z.getDegat());
            }

            if (z.prendreRecompense()) {
                argent.set(getArgent() + RecompenseParZombie);
            }

            if (z instanceof ZombieFamille zf) {
                if (zf.estMort()) {
                    // Les sous-unités apparaissent sur la case exacte du parent pour suivre la suite du chemin
                    List<Enemie> enfants = zf.genererEnfants(chemin, zf.getEtapeActuelle());
                    zombies.addAll(enfants);
                }
            }

            // Un seul bonus à la fois : rien au sol et aucun effet en cours
            if (z.estMort() && this.objets.isEmpty() && this.bonusActif == null && Objet.lacher()) {
                this.objets.add(new Objet(z.getX(), z.getY(), this));
            }

            if (z.estMort() || z.estArrive()) {
                zombies.remove(i);
            }
        }
    }

    private void gererTransitionVague() {
        if (vagueEnCours.get() && zombiesRestantsASpawner == 0 && zombies.isEmpty()) {
            vagueEnCours.set(false);
            tempsAvantProchaineVague = DelaientreVague;
        }

        if (!vagueEnCours.get() && getNumeroVague() > 0) {
            tempsAvantProchaineVague--;
            if (tempsAvantProchaineVague <= 0) {
                preparerNouvelleVague();
            }
        }
    }

    public boolean payerAchat(int montant) {
        if (getArgent() >= montant) {
            argent.set(getArgent() - montant);
            return true;
        }
        return false;
    }

    private void faireApparaitreZombie() {
        Point depart = terrain.trouverEntree();
        int pixelX = (int) (depart.y * TailleCase);
        int pixelY = (int) (depart.x * TailleCase);

        Enemie zombie = strategieApparition.creerZombie(pixelX, pixelY, zombiesRestantsASpawner, Math.random());
        zombie.setChemin(chemin);
        zombies.add(zombie);
    }

    private StrategieApparition choisirStrategie(int vague) {
        if (vague == NombreVaguesMax) return new ApparitionFinale();
        if (vague >= 5) return new ApparitionAleatoire();
        if (vague >= 3) return new ApparitionGros();
        if (vague >= 2) return new ApparitionRapide();
        return new ApparitionNormale();
    }

    public void vendreTour(int pixelX, int pixelY) {
        Tour tour = trouverTourEn(pixelX, pixelY);
        if (tour == null) return;                            // rien à vendre ici

        int remboursement = tour.getType().getPrix() / 2;
        argent.set(getArgent() + remboursement);

        if (estSurChemin(idTuileEn(pixelX, pixelY))) {
            compteurToursSurChemin--;                        // une place se libère sur le chemin
        }

        tours.remove(tour);
        System.out.println("Tour vendue ! Recrédité de : " + remboursement + " Tickets.");
    }

    public void ameliorerTour(int pixelX, int pixelY) {
        Tour tour = trouverTourEn(pixelX, pixelY);
        if (tour == null) return;                            // rien à améliorer ici

        int cout = tour.getPrixAmelioration();
        if (payerAchat(cout)) {
            tour.ameliorer();
            System.out.println("Tour améliore au niveau " + tour.getNiveau() + " ! Cost: " + cout);
        } else {
            System.out.println("Pas assez d'argent pour améliorer cette tour !");
        }
    }

    public boolean toutesVaguesTerminees() {
        return getNumeroVague() >= NombreVaguesMax && !vagueEnCours.get() && zombies.isEmpty();
    }

    public ObservableList<Enemie> getZombies() { return zombies; }
    public ObservableList<Tour> getTours() { return tours; }
    public ObservableList<Projectile> getProjectiles() { return this.projectiles; }
    public ObservableList<Objet> getObjets() { return this.objets; }
    public Terrain getTerrain() { return terrain; }
    public Comptoir getBase() { return base; }

    public int getArgent() { return argent.get(); }
    public IntegerProperty argentProperty() { return argent; }

    public int getNumeroVague() { return numeroVague.get(); }
    public IntegerProperty numeroVagueProperty() { return numeroVague; }
    public BooleanProperty vagueEnCoursProperty() { return vagueEnCours; }

    public BooleanProperty defaiteProperty() { return defaite; }
    public BooleanProperty victoireProperty() { return victoire; }
    public boolean isPartieTerminee() { return defaite.get() || victoire.get(); }

    public IntegerProperty tempsbonusProperty() { return this.tempsbonus; }
    public int getTempsbonus() { return this.tempsbonus.get(); }
    public void setTempsbonus(int temps) { this.tempsbonus.set(temps); }
}
