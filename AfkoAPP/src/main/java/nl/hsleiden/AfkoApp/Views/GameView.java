package nl.hsleiden.AfkoApp.Views;

import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.util.Duration;
import nl.hsleiden.AfkoApp.Controllers.AdminController;
import nl.hsleiden.AfkoApp.Controllers.GameController;
import nl.hsleiden.AfkoApp.Controllers.ReportController;
import nl.hsleiden.AfkoApp.Models.*;
import nl.hsleiden.AfkoApp.Models.Department;
import nl.hsleiden.AfkoApp.Models.GameQuestion;
import java.util.ArrayList;
import java.util.Collection;

public class GameView {

    private GameController gameController;
    private AdminController adminController;
    private ReportController reportController;
    private int maxSeconds = 60;
    private Timeline timeline;
    private Label timerLabel;
    private String username;


    @FXML
    private ComboBox departmentfilter;
    @FXML
    private TextField usernameTextField;
    @FXML
    private Pane testPane, game, innerPane;
    @FXML
    private HBox gamepane;
    @FXML
    private VBox ranks, scores, names, reports;
    @FXML
    private StackPane root;
    @FXML
    private BorderPane pane;

    private final Image REPORT_IMAGE = new Image("report.png");

    /**
     * Runs necessary dependencies on load.
     * @author InsectByte
     */
    @FXML
    protected void initialize() {
        gameController = GameController.getInstance();
        reportController = ReportController.getInstance();
        adminController = AdminController.getInstance();
        updateDepartments(gameController.getDepartments());
        updateScoreboard(gameController.getHighscores());
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                usernameTextField.requestFocus();
            }
        });
    }

    /**
     * Switches scene to Afkotheek
     * @author InsectByte
     */
    public void switchToAfkotheek() {
        gameController.switchScene("home");
    }

    /**
     * Switches scene to Submit
     * @author InsectByte
     */
    public void switchToSubmit() {
        gameController.switchScene("submit");
    }

    /**
     * Opens login Pane, switches scene to admin if credentials are valid
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
                gameController.switchScene("admin");
            }
        });
        close.setOnMouseClicked(event -> {
            root.getChildren().remove(gridPane);
        });
    }

    /**
     * Updates available departments.
     * @author InsectByte
     * @param departments
     */
    private void updateDepartments(ArrayList<Department> departments) {
        departmentfilter.getItems().clear();
        departmentfilter.getItems().add("Allemaal");
        departmentfilter.setValue(departmentfilter.getItems().get(0));
        for (Department dep : departments) {
            departmentfilter.getItems().add(dep.getDepname());
        }
    }

    /**
     * Checks user input and throws an error or goes on to initializeGame()
     * @Author RamonK
     */
    @FXML
    private void startGame() {
        username = usernameTextField.getText().strip();
        if (username.equals("")) {
            JFXSnackbar bar = new JFXSnackbar(game);
            bar.setPrefWidth(game.getWidth());
            String snackbarText = "Vul je gebruikersnaam in om het spel te starten.";
            bar.fireEvent(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout(snackbarText), Duration.seconds(2.5)));
        }else {
           initializeGame();
        }

    }

    /**
     * Sets the department for question retrieval
     *
     * @Author RamonK
     */
    private void initializeGame() {
        String department = departmentfilter.getValue().toString().trim();
        gameController.startGame(department);
        gamepane.getChildren().clear();
        createTimer();
        createQuestion();

    }

    /**
     * Retrieves a question from the gamecontroller
     * @Author RamonK
     *
     */
    private void createQuestion() {
        GameQuestion question = gameController.nextQuestion();

        if (question == null) {
            gameFinished();
        }
        else{
            displayQuestion(question);
        }
    }

    /**
     * Displays the next question and updates the displayed score when the user answers the question right.
     * @author Ramon Krijnen, Rick Ketelaar
     * @param question
     */
    private void displayQuestion(GameQuestion question) {
        VBox questionBox = new VBox();
        questionBox.setPrefWidth((gamepane.getWidth() * 0.8));
        questionBox.setStyle("-fx-alignment: center center;" +
                             "-fx-spacing: 2em;");

        Label definitionLabel = new Label(question.getDescription());
        definitionLabel.setStyle("-fx-font-weight: 900;");

        TextField answerField = new TextField();
        answerField.setOnAction(event -> {
            checkAnswer(question, answerField.getText());
        });

        Label scoreLabel = new Label();
        String scoreText = String.format("Score: %s", gameController.getGameScore());
        scoreLabel.setText(scoreText);

        Button confirmButton = new Button("Volgende");
        confirmButton.setOnAction(e -> checkAnswer(question, answerField.getText()));

        questionBox.getChildren().addAll(definitionLabel,answerField,confirmButton, scoreLabel);
        gamepane.getChildren().add(0,questionBox);
        answerField.requestFocus();
    }

    /**
     * Checks if user input is equal to  .
     * @author Rick Ketelaar
     * @param answer
     * @param gameQuestion
     */
    private void checkAnswer(GameQuestion gameQuestion, String answer) {
        gamepane.getChildren().remove(0);
        gameController.checkAnswer(gameQuestion, answer);

        createQuestion();
    }

    /**
     * Updates the scoreboard by clearing the visible scores and updating them with new ones.
     * @author Rick Ketelaar
     */
    private void updateScoreboard(ArrayList<GameScore> highscores){
        ranks.getChildren().clear();
        names.getChildren().clear();
        scores.getChildren().clear();
        reports.getChildren().clear();

        ArrayList<Object> nodes = createHighscores(highscores);
        ranks.getChildren().addAll((Collection<Label>) nodes.get(0));
        names.getChildren().addAll((Collection<Label>) nodes.get(1));
        scores.getChildren().addAll((Collection<Label>) nodes.get(2));
        reports.getChildren().addAll((Collection<ImageView>) nodes.get(3));
    }

    /**
     * Creates the highscore labels that are visible on the screen.
     * Ranks scores from high to low and allows user to report usernames by clicking on the image
     * @author Rick Ketelaar
     * @param highscores
     * @return ArrayList<Object>
     */
    private ArrayList<Object> createHighscores(ArrayList<GameScore> highscores){
        Collection<Label> rankLabels = new ArrayList<>();
        Collection<Label> usernameLabels = new ArrayList<>();
        Collection<Label> scoreLabels = new ArrayList<>();
        Collection<ImageView> reportImageViews = new ArrayList<>();
        for (int i = 1; i <= highscores.size(); i++){
            rankLabels.add(new Label(""+i));
        }
        for (GameScore gameScore : highscores) {
            usernameLabels.add(new Label(gameScore.getUsername()));
            scoreLabels.add(new Label(""+gameScore.getScore()));
            ImageView imageView = new ImageView(REPORT_IMAGE);
            imageView.setFitHeight(27);
            imageView.setPreserveRatio(true);
            imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> reportUsername(gameScore));
            reportImageViews.add(imageView);
        }
        ArrayList<Object> results = new ArrayList<>();
        results.add(rankLabels);
        results.add(usernameLabels);
        results.add(scoreLabels);
        results.add(reportImageViews);
        return results;
    }

    /**
     * Adds a report to an inappropriate username.
     * @author Rick Ketelaar
     * @param gameScore
     */
    private void reportUsername(GameScore gameScore){
        reportController.addReport(gameScore, ""); // The empty string is the message. This is not implemented in Front end

        JFXSnackbar bar = new JFXSnackbar(pane);
        bar.setPrefWidth(pane.getWidth());
        String snackbarText = "Bedankt voor het rapporteren, de username zal zo snel mogelijk beoordeeld worden.";
        bar.fireEvent(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout(snackbarText), Duration.seconds(2.5)));
    }


    /**
     * Creates a visual timer that runs when the game is started
     * @Author RamonK
     */
    private void createTimer() {
        HBox timerBox = new HBox();

        timerLabel = new Label("60");
        timerLabel.getStyleClass().add("game-timer");
        timerLabel.setAlignment(Pos.TOP_RIGHT);
        timerLabel.setStyle("-fx-spacing: 50px;" +
                            "-fx-border-radius: 100%;" +
                            "-fx-border-insets: 3 3 3 3;" +
                            "-fx-border-color: #ffa500;" +
                            "-fx-text-fill: #555;" +
                            "-fx-font-size: 3em;");

        timerBox.getChildren().add(timerLabel);
        gamepane.getChildren().add(timerBox);

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), actionEvent -> {
            timerLabel.setText(String.valueOf(maxSeconds));
            maxSeconds--;
        }));
        timeline.setCycleCount(maxSeconds);
        timeline.setOnFinished(event -> {
            gamepane.getChildren().remove(0);
            gameFinished();
        } );
        timeline.play();
    }

    /**
     * Creates the end screen, once the game is finished
     * @Author RamonK
     */
    private void gameFinished() {
        gameController.addGamescore(username);
        timeline.stop();
        gamepane.getChildren().remove(0,1);

        VBox resultBox = new VBox();
        resultBox.setPrefWidth(innerPane.getPrefWidth());

        String congratsString = String.format("Goed gedaan %s!",username);
        String scoreString = String.format("Je hebt %s punten gescoord!", gameController.getGameScore());

        HBox logoBox = new HBox();
        Label afkoLabel = new Label("AFKO");
        afkoLabel.setStyle("-fx-text-fill: #ffa500; -fx-font-size: 2.6em;");

        Label gameLabel = new Label("Game");
        gameLabel.setStyle("-fx-text-fill: #808080; -fx-font-size: 2.6em");

        logoBox.getChildren().addAll(afkoLabel, gameLabel);

        VBox scoreAndNameBox = new VBox();
        scoreAndNameBox.setSpacing(25);
        scoreAndNameBox.setAlignment(Pos.CENTER);
        Label congratsLabel = new Label(congratsString);
        Label scoreLabel = new Label(scoreString);

        Button exitButton = new Button("Verlaat spel");
        exitButton.setOnAction(e -> gameController.switchScene("game"));
        exitButton.setStyle("-fx-alignment: center center");

        scoreAndNameBox.getChildren().addAll(congratsLabel, scoreLabel,exitButton);
        scoreAndNameBox.setStyle("-fx-spacing: 25px");
        resultBox.getChildren().addAll(logoBox, scoreAndNameBox);
        gamepane.getChildren().add(resultBox);
    }
}
