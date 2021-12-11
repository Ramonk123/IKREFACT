package nl.hsleiden.AfkoApp.Views;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.util.Duration;
import nl.hsleiden.AfkoApp.Controllers.AdminController;
import nl.hsleiden.AfkoApp.Controllers.HomeController;
import nl.hsleiden.AfkoApp.Controllers.ReportController;
import nl.hsleiden.AfkoApp.Models.Abbreviation;
import nl.hsleiden.AfkoApp.Models.Department;
import nl.hsleiden.AfkoApp.Models.Home;
import nl.hsleiden.AfkoApp.Observers.HomeObserver;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;
import com.jfoenix.controls.JFXSnackbarLayout;
import java.util.ArrayList;
import java.util.Collection;

public class HomeView implements HomeObserver {

    @FXML
    private TextField searchBox; // Contains Abbreviation Search Query
    @FXML
    private ComboBox departmentFilter; // Contains the department filter option. Default (should be): Rijksbreed
    @FXML
    private VBox abbrs, defs, newabbrs, newdefs, reports, newreports; // Should contain all matching result. (Maybe filled with HBoxes or someting?)
    @FXML
    private StackPane root;
    @FXML
    private BorderPane pane;

    private HomeController homeController;
    private ReportController reportController;
    private AdminController adminController;

    private final Image REPORT_IMAGE = new Image("report.png");

    /**
     * Loads all necessary dependencies on Load
     * @author InsectByte
     */
    @FXML
    protected void initialize() {
        reportController = ReportController.getInstance();
        homeController = HomeController.getInstance();
        adminController = AdminController.getInstance();

        homeController.registerHomeObserver(this);
        homeController.getDepartments();
        homeController.getRecents();
        updateDepartments(homeController.getDepartments());
    }

    /**
     * Switches scene to Submit
     * @author InsectByte
     */
    public void switchToSubmit() {
        homeController.switchScene("submit");
    }

    /**
     * Switches scene to Game
     * @author InsectByte
     */
    public void switchToGame() {
        homeController.switchScene("game");
    }

    /**
     * Opens admin login pane. Switches scene to admin if credentials are valid.
     * @author InsectByte
     */
    public void switchToAdmin() {
        GridPane gridPane = new GridPane();
        TextField username = new TextField();
        PasswordField password = new PasswordField();
        Button signIn = new Button("Log In");
        Button close = new Button("Sluit");
        HBox hBox = new HBox();
        gridPane.setMinHeight(pane.getHeight()*0.3);
        gridPane.setMinWidth(pane.getWidth()*0.3);
        gridPane.setMaxHeight(pane.getHeight()*0.3);
        gridPane.setMaxWidth(pane.getWidth()*0.3);
        gridPane.setPadding(new Insets(25,25,25,25));
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.add(new Label("Gebruikersnaam: "), 0, 1);
        gridPane.add(username, 1, 1);
        gridPane.add(new Label("Wachtwoord: "), 0, 2);
        gridPane.add(password, 1, 2);
        hBox.setAlignment(Pos.BOTTOM_RIGHT);
        hBox.getChildren().addAll(close, signIn);
        gridPane.add(hBox, 1, 4);
        gridPane.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1px;");
        root.getChildren().add(gridPane);
        signIn.setOnMouseClicked(event -> {
            adminController.login(username.getText(), password.getText());
            if (adminController.getJWT() != null) {
                homeController.switchScene("admin");
            }
        });
        close.setOnMouseClicked(event -> {
            root.getChildren().remove(gridPane);
        });
    }

    @FXML
    private void findAbbreviations() {
        if (searchBox.getText().strip().equals("")) {return;}
        homeController.getResults(searchBox.getText(), departmentFilter.getValue().toString());

    }

    /**
     * Updates available Departments
     * @author InsectByte
     * @param departments
     */
    void updateDepartments(ArrayList<Department> departments) {
        departmentFilter.getItems().add("Allemaal");
        departmentFilter.setValue(departmentFilter.getItems().get(0));
        for (Department dep : departments) {
            departmentFilter.getItems().add(dep.getDepname());
        }
    }

    /**
     * Updates recent abbreviations
     * @author InsectByte, Max, Ramon
     * @param abbreviations
     */
    void updateRecentAbbreviations(ArrayList<Abbreviation> abbreviations) {
        newabbrs.getChildren().clear();
        newdefs.getChildren().clear();
        newreports.getChildren().clear();

        ArrayList<Object> nodes = createAbbreviations(abbreviations);
        newabbrs.getChildren().addAll((Collection<Label>) nodes.get(0));
        newdefs.getChildren().addAll((Collection<Label>) nodes.get(1));
        newreports.getChildren().addAll((Collection<ImageView>) nodes.get(2));
    }

    void updateAbbreviations(ArrayList<Abbreviation> abbreviations) {
        abbrs.getChildren().clear();
        defs.getChildren().clear();
        reports.getChildren().clear();

        ArrayList<Object> nodes = createAbbreviations(abbreviations);
        abbrs.getChildren().addAll((Collection<Label>) nodes.get(0));
        defs.getChildren().addAll((Collection<Label>) nodes.get(1));
        reports.getChildren().addAll((Collection<ImageView>) nodes.get(2));
    }

    public ArrayList<Object> createAbbreviations(ArrayList<Abbreviation> abbreviations) {

        Collection<Label> abbreviationLabels = new ArrayList<>();
        Collection<Label> definitionLabels = new ArrayList<>();
        Collection<ImageView> reportImageViews = new ArrayList<>();

        for (Abbreviation abbreviation : abbreviations) {
            ImageView imageView = new ImageView(REPORT_IMAGE);
            imageView.setFitHeight(27);
            imageView.setPreserveRatio(true);
            imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> reportAbbreviation(abbreviation));

            abbreviationLabels.add(new Label(abbreviation.getAbbreviation_name()));
            definitionLabels.add(new Label(abbreviation.getDefinition()));
            reportImageViews.add(imageView);
        }

        ArrayList<Object> results = new ArrayList<>();
        results.add(abbreviationLabels);
        results.add(definitionLabels);
        results.add(reportImageViews);
        return results;
    }

    /**
     * Sends a report when icon is pressed
     * @author RamonK
     * @param abbreviation
     */
    public void reportAbbreviation(Abbreviation abbreviation) {
        reportController.addReport(abbreviation, ""); // The empty string is the message. This is not implemented in Front end

        JFXSnackbar bar = new JFXSnackbar(pane);
        bar.setPrefWidth(pane.getWidth());
        String snackbarText = "Bedankt voor het rapporteren, de afkorting zal zo snel mogelijk beoordeeld worden.";
        bar.fireEvent(new SnackbarEvent(new JFXSnackbarLayout(snackbarText), Duration.seconds(2.5)));
    }

    public void update(Home home) {
        updateAbbreviations(home.getResultAbbreviations());
        updateRecentAbbreviations(home.getRecentAbbreviations());
    }

}
