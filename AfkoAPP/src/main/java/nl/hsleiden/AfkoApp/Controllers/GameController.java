package nl.hsleiden.AfkoApp.Controllers;

import nl.hsleiden.AfkoApp.App;
import nl.hsleiden.AfkoApp.DAO.GameDAO;
import nl.hsleiden.AfkoApp.Models.Department;
import nl.hsleiden.AfkoApp.Models.GameQuestion;
import nl.hsleiden.AfkoApp.Models.GameScore;

import java.util.ArrayList;
import java.util.UUID;

public class GameController {
    private static GameController gameController;
    private GameDAO gameDAO;

    App app;
    AbbreviationController abbreviationController;
    DepartmentController departmentController;

    ArrayList<GameQuestion> questions = new ArrayList<>();
    int questionIndex = -1;
    private int gameScore = 0;

    /**
     * Creates instance of GameController if it does not exist. Returns instance.
     * @author InsectByte
     * @return GameController
     */
    public static synchronized GameController getInstance() {
        if(gameController == null) {
            gameController = new GameController();
        }
        return gameController;
    }

    /**
     * Loads all necessary dependencies on other Controllers.
     * @author InsectByte
     */
    void getDependencies() {
        app = App.getInstance();
        departmentController = DepartmentController.getInstance();
        abbreviationController = AbbreviationController.getInstance();
        gameDAO = GameDAO.getInstance();
    }

    /**
     * Constructor for GameController
     * @author InsectByte
     */
    private GameController() {
        getDependencies();
    }

    /**
     * Gets all departments from DepartmentController
     * @author InsectByte
     * @return ArrayList<Department>
     */
    public ArrayList<Department> getDepartments() {
        return departmentController.getDepartments();
    }

    /**
     * Calls the gameDAO to obtain questions.
     * @author Rick Ketelaar
     * @return ArrayList<GameQuestion>
     */
    public ArrayList<GameQuestion> getQuestions() {
        return gameDAO.fetchGameQuestions();
    }

    /**
     * Gets an ArrayList of GameQuestion objects sorted by department ID.
     * @author Rick Ketelaar
     * @param departmentId
     * @return ArrayList<GameQuestion>
     */
    public ArrayList<GameQuestion> getQuestions(UUID departmentId) {
        return gameDAO.fetchGameQuestionsByDepId(departmentId);
    }

    public int getGameScore() {
        return gameScore;
    }

    /**
     * Starts the AfkoGame
     * @Author RamonK
     * @param departmentName
     */
    public void startGame(String departmentName) {
        gameScore = 0;
        questionIndex = -1;
        questions.clear();

        if (departmentName.equals("Allemaal")) {
            this.questions = getQuestions();
        } else {
            UUID depId = departmentController.fetchDepartmentByDepartmentName(departmentName).getId();
            this.questions = getQuestions(depId);


        }
    }

    /**
     * Retrieves a question for the AfkoGame and sends it to the view
     * @Author
     * @return
     */
    public GameQuestion nextQuestion() {
        questionIndex++;
        if (questionIndex == questions.size()) {
            return null;
        }
        return questions.get(questionIndex);
    }

    /**
     * Switches scene to supplied scenename. Generates new GameModel.
     * @author InsectByte
     * @param scene
     */
    public void switchScene(String scene) {
        app.switchScene(scene);
    }

    /**
     * Checks if answer submitted by the user is equal to the abbreviation name. Assures question and answer are lowercase.
     * @author Rick Ketelaar
     * @param answer
     * @param question
     */
    public void checkAnswer(GameQuestion question, String answer) {
        String questionDefinition = question.getAbbreviationName().strip().toLowerCase();
        String userAnswer = answer.strip().toLowerCase();
        if (questionDefinition.equals(userAnswer)) {
                this.gameScore++;
        }
    }

    /**
     * Gets an ArrayList of GameScore objects sorted by highscore.
     * @author Rick Ketelaar
     * @return ArrayList<GameScore>
     */
    public ArrayList <GameScore> getHighscores(){
        return gameDAO.fetchGameScores(true, 10);
    }

    public GameScore addGamescore(String username){
        GameScore gameScore = new GameScore(username, this.gameScore);
        gameDAO.addGameScore(gameScore);
        return gameScore;
    }

    public boolean removeGameScore(GameScore gameScore) {
        return gameDAO.deleteGameScore(gameScore.toJSONObject());
    }
}
