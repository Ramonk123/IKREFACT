package nl.hsleiden.AfkoApp.Views;

import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import nl.hsleiden.AfkoApp.Controllers.AdminController;
import nl.hsleiden.AfkoApp.Models.AbbreviationReport;
import nl.hsleiden.AfkoApp.Models.AdminPanel;
import nl.hsleiden.AfkoApp.Models.GameScoreReport;
import nl.hsleiden.AfkoApp.Observers.AdminObserver;

import java.util.ArrayList;
import java.util.Collection;

/**
 * The view of the admin.
 * @author Max Mulder, Daniel Paans
 */
public class AdminPanelView implements AdminObserver {

    private AdminController adminController;

    @FXML
    private Pane pane;

    @FXML
    private ScrollPane abbreviations, gameScores;

    @FXML
    private VBox gameScoresVBox, abbreviationsVBox;

    @FXML
    private TextArea textArea;

    @FXML
    private Button uploadCSV;

    private String token;

    private final Image DELETE_IMAGE = new Image("delete.png");
    private final Image ACCEPT_IMAGE = new Image("accept.png");
    private final int IMAGE_SIZE = 52;
    private final int[] SIZE = {370, 440};

    @FXML
    protected void initialize() {
        adminController = AdminController.getInstance();

        adminController.registerAdminObserver(this);
        adminController.getAbbreviationReports();
        adminController.getGameScoreReports();
    }

    @FXML
    public void switchToAfkotheek() {
        adminController.switchScene("home");
    }

    @FXML
    public void switchToSubmit() {
        adminController.switchScene("submit");
    }

    @FXML
    public void switchToGame() {
        adminController.switchScene("game");
    }

    /**
     * creates a collection of panes based on gameScoreReports
     * @param gameScoreReports
     * @return
     * @author Max Mulder
     */
    public Collection<Pane> createGameScoreReports(ArrayList<GameScoreReport> gameScoreReports) {

        Collection<Pane> reportPanes = new ArrayList<>();
        for (GameScoreReport report : gameScoreReports) {
            VBox vBox = new VBox();
            vBox.setSpacing(-8);
            vBox.setPrefWidth(SIZE[1]);
            vBox.getChildren().add(new Label("Verwijder Highscore?"));
            vBox.getChildren().add(new Label(String.format("Gebruikersnaam: %s", report.getGameScore().getUsername()) ));
            vBox.getChildren().add(new Label(String.format("Score: %s", report.getGameScore().getScore()) ));
            vBox.getChildren().add(new Label(String.format("Gerapporteerd: %s keer", report.getAmount())));

            ImageView acceptImageView = new ImageView(ACCEPT_IMAGE);
            acceptImageView.setFitHeight(IMAGE_SIZE);
            acceptImageView.setPreserveRatio(true);
            acceptImageView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> deleteGameScoreByReport(report));

            ImageView deleteImageView = new ImageView(DELETE_IMAGE);
            deleteImageView.setFitHeight(IMAGE_SIZE);
            deleteImageView.setPreserveRatio(true);
            deleteImageView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> deleteGameScoreReport(report));

            HBox hBox = new HBox();
            hBox.getChildren().add(vBox);
            hBox.getChildren().add(acceptImageView);
            hBox.getChildren().add(deleteImageView);

            Pane pane = new Pane();
            pane.getStyleClass().add("adminDisplayCard");
            pane.getChildren().add(hBox);

            reportPanes.add(pane);
        }
        return reportPanes;
    }

    /**
     * creates a collection of panes based on abbreviationReports
     * @param abbreviationReports
     * @return
     * @author Max Mulder
     */
    public Collection<Pane> createAbbreviationReports(ArrayList<AbbreviationReport> abbreviationReports) {

        Collection<Pane> reportPanes = new ArrayList<>();
        for (AbbreviationReport report : abbreviationReports) {
            VBox vBox = new VBox();
            vBox.setSpacing(-8);
            vBox.setPrefWidth(SIZE[0]); // Hardcoded for 1920x1080

            Label definitie = new Label(String.format("Definitie: %s", report.getAbbreviation().getDefinition()));
            definitie.setWrapText(true);

            vBox.getChildren().add(new Label("Verwijder Afkorting?"));
            vBox.getChildren().add(new Label(String.format("Naam: %s", report.getAbbreviation().getAbbreviation_name())));
            vBox.getChildren().add(definitie);
            vBox.getChildren().add(new Label(String.format("Gerapporteerd: %s keer", report.getAmmount())));

            ImageView acceptImageView = new ImageView(ACCEPT_IMAGE);
            acceptImageView.setFitHeight(IMAGE_SIZE);
            acceptImageView.setPreserveRatio(true);
            acceptImageView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> deleteAbbreviationByReport(report));

            ImageView deleteImageView = new ImageView(DELETE_IMAGE);
            deleteImageView.setFitHeight(IMAGE_SIZE);
            deleteImageView.setPreserveRatio(true);
            deleteImageView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> deleteAbbreviationReport(report));

            HBox hBox = new HBox();
            hBox.getChildren().add(vBox);
            hBox.getChildren().add(acceptImageView);
            hBox.getChildren().add(deleteImageView);

            Pane pane = new Pane();
            pane.getStyleClass().add("adminDisplayCard");
            pane.getChildren().add(hBox);

            reportPanes.add(pane);
        }
        return reportPanes;
    }

    public void updateTextArea() {
        uploadCSV.setDisable(textArea.getText().length() <= 0);
    }

    /**
     * Opens the file explorer window and opens the selected text file.
     * @author Max Mulder
     */
    public void openCSV() {
        try {
            textArea.setText(adminController.OpenCSV());
            uploadCSV.setDisable(false);
        } catch (Exception e) {
            // File not found
            // e.printStackTrace();
        }
    }

    /**
     * Sends uploads the csv entered in the textfield
     * @author Max Mulder
     */
    public void submitCSV() {
        if (adminController.uploadCSV(textArea.getText())) {
            textArea.clear();
            uploadCSV.setDisable(true);
            snackbar("Afkortingen toegevoegd.");
        } else {
            snackbar("Er ging iets mis. Probeer het opniew.");
        }
    }

    /**
     * Deletes gameScores based on a report
     * @param report
     * @author Max Mulder
     */
    public void deleteGameScoreByReport(GameScoreReport report) {
        if (adminController.deleteGameScoreByReport(report)) {
            snackbar("De score is uit de database verwijderd.");
        } else {
            snackbarmis();
        }
    }

    /**
     * Deletes gameScoresReport based on a report
     * @param report
     * @author Max Mulder
     */
    public void deleteGameScoreReport(GameScoreReport report) {
        if (adminController.deleteGameScoreReport(report)) {
            snackbar("Het rapport is afgekeurd en de score is niet verwijderd. ");
        } else {
            snackbarmis();
        }
    }

    /**
     * Deletes abbreviation based on a report
     * @param report
     * @author Max Mulder
     */
    public void deleteAbbreviationByReport(AbbreviationReport report) {
        if (adminController.deleteAbbreviationByReport(report)) {
            snackbar("Afkorting is uit de database verwijderd.");
        } else {
            snackbarmis();
        }
    }

    /**
     * Deletes abbreviationReport based on a report
     * @param report
     * @author Max Mulder
     */
    public void deleteAbbreviationReport(AbbreviationReport report) {
        if (adminController.deleteAbbreviationReport(report)) {
            snackbar("Rapport is afgekeurd en de afkorting is niet uit de database verwijderd.");
        } else {
            snackbarmis();
        }
    }


    private void snackbarmis() {
        snackbar("Er ging iets mis. Probeer het later opniew.");
    }

    private void snackbar(String text) {
        JFXSnackbar bar = new JFXSnackbar(pane);
        bar.setPrefWidth(pane.getWidth());
        bar.fireEvent(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout(text), Duration.seconds(3.2)));
    }

    private void updateReports(ArrayList<GameScoreReport> gameScoreReports,
                          ArrayList<AbbreviationReport> abbreviationReports) {
        this.gameScoresVBox.getChildren().clear();
        this.abbreviationsVBox.getChildren().clear();
        Collection<Pane> abbreviationNodes = createAbbreviationReports(abbreviationReports);
        Collection<Pane> gameScoreNodes = createGameScoreReports(gameScoreReports);
        this.abbreviationsVBox.getChildren().addAll(abbreviationNodes);
        this.gameScoresVBox.getChildren().addAll(gameScoreNodes);

    }

    /**
     * Updates the adminPanelView when there are changes amount of reports.
     * @author Daniel Paans
     * @param admin
     */
    @Override
    public void update(AdminPanel admin) {
        updateReports(admin.getGameScoreReports(), admin.getAbbreviationReports());
    }

}
