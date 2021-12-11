package nl.hsleiden.AfkoApp.Views;

import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;
import nl.hsleiden.AfkoApp.Controllers.AbbreviationController;
import nl.hsleiden.AfkoApp.Controllers.AdminController;
import nl.hsleiden.AfkoApp.Controllers.SubmitController;
import nl.hsleiden.AfkoApp.Models.Abbreviation;
import nl.hsleiden.AfkoApp.Models.Department;
import nl.hsleiden.AfkoApp.Models.Submit;
import nl.hsleiden.AfkoApp.Observers.SubmitObserver;

import java.util.ArrayList;

public class SubmitView implements SubmitObserver {

    private SubmitController submitController;

    private AbbreviationController abbreviationController;
    private AdminController adminController;

    @FXML
    private TextField abbreviation, definition;
    @FXML
    private ComboBox department;
    @FXML
    private CheckBox confirm;
    @FXML
    private VBox resultabbrs, resultdeps;
    @FXML
    private Button submitButton;
    @FXML
    private StackPane root;
    @FXML
    private BorderPane pane;

    @FXML
    protected void initialize() {
        submitController = submitController.getInstance();
        adminController = AdminController.getInstance();
        submitController.registerSubmitObserver(this);
        submitController.getDepartments();
        submitButton.setDisable(true);
        abbreviationController = AbbreviationController.getInstance();
        updateDepartments(submitController.getDepartments());
    }

    /**
     * Switches scene to Afkotheek
     * @author InsectByte
     */
    @FXML
    private void switchToAfkotheek() {
        submitController.switchScene("home");
    }

    /**
     * Switches scene to Game
     * @author InsectByte
     */
    @FXML
    private void switchToGame() {
        submitController.switchScene("game");
    }

    /**
     * Opens admin login panel, switches scene to admin if credentials are valid.
     * @author InsectByte
     */
    @FXML
    private void switchToAdmin() {
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
                submitController.switchScene("admin");
            }
        });
        close.setOnMouseClicked(event -> {
            root.getChildren().remove(gridPane);
        });
    }

    /**
     * Updates available departments
     * @author InsectByte
     * @param departments
     */
    private void updateDepartments(ArrayList<Department> departments) {
        for (Department dep : departments) {
            department.getItems().add(dep.getDepname());
        }
        department.setValue(departments.get(0).getDepname());
    }

    public void update(Submit submit) {
        displayAbbreviations(submit.getAbbreviationResults());
    }

    /**
     * Disables the button when checkbox is not selected
     * @Author RamonK
     * @param actionEvent
     */
    public void setButtonInCorrectState(ActionEvent actionEvent) {
        submitButton.setDisable(!confirm.isSelected());
    }
    @FXML
    public void findAbbreviations() {
        if (!abbreviation.getText().strip().equals("")) {
            submitController.setAbbreviationResults(abbreviation.getText());
        }
    }

    /**
     * Displays abbreviation when entering keyword
     * @Author RamonK
     * @param abbreviations
     */
    public void displayAbbreviations(ArrayList<Abbreviation> abbreviations) {
        resultabbrs.getChildren().clear();
        resultdeps.getChildren().clear();
        for (Abbreviation abbreviation : abbreviations) {
            String abbreviationName = abbreviation.getAbbreviation_name();
            resultabbrs.getChildren().add(new Label(abbreviationName));

            for (Department department : abbreviation.getDepartments()) {
                String departmentName = department.getDepname();
                resultdeps.getChildren().add(new Label(departmentName));
            }
        }
    }

    /**
     * Submits an abbreviation to the back-end
     * @Author RamonK
     * @param actionEvent
     */
    public void submitAbbreviation(ActionEvent actionEvent) {
        JFXSnackbar bar = new JFXSnackbar(pane);
        if (isTextfieldEmpty(abbreviation) && isTextfieldEmpty(definition)) {
            bar.setPrefWidth(pane.getWidth());
            bar.fireEvent(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("Er ontbreken nog gegegevens, probeer het opnieuw!"), Duration.seconds(2.5)));
        }
        else{
            submitController.submitAbbreviation(abbreviation.getText(), definition.getText(),
                    department.getValue().toString());
            bar.setPrefWidth(pane.getWidth());
            bar.fireEvent(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("Bedankt voor het indienen!"), Duration.seconds(2.5)));
            abbreviation.clear();
            definition.clear();
        }
    }

    public boolean isTextfieldEmpty(TextField textField) {
        return textField.getText().strip().equals("");
    }
}
