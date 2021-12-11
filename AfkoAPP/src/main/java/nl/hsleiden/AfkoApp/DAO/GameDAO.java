package nl.hsleiden.AfkoApp.DAO;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import nl.hsleiden.AfkoApp.Controllers.DepartmentController;
import nl.hsleiden.AfkoApp.Controllers.DotEnvController;
import nl.hsleiden.AfkoApp.Models.GameQuestion;
import nl.hsleiden.AfkoApp.Models.GameScore;
import nl.hsleiden.AfkoApp.Models.GameScoreReport;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.UUID;

public class GameDAO {

    private static GameDAO gameDAO;

    DotEnvController dotEnvController;
    DepartmentController departmentController;
    private ArrayList<GameScore> gameScores;
    private ArrayList<GameScoreReport> gameScoreReports;
    private ArrayList<GameQuestion> gameQuestions = new ArrayList<>();
    private final int MAX_QUESTIONS = 50;

    public static synchronized GameDAO getInstance() {
        if (gameDAO == null) {
            gameDAO = new GameDAO();
        }
        return gameDAO;
    }

    /**
     * Constructor for GameDAO
     * @author InsectByte
     */
    private GameDAO() {
        dotEnvController = DotEnvController.getInstance();
        departmentController = DepartmentController.getInstance();
    }



    public ArrayList<GameScore> fetchGameScores() {
        return fetchGameScores(false, -1);
    }


    public ArrayList<GameScore> fetchGameScores(boolean distinct) {
        return fetchGameScores(distinct, -1);
    }


    public ArrayList<GameScore> fetchGameScores(int limit) {
        return fetchGameScores(false, limit);
    }


    public ArrayList<GameScore> fetchGameScores(boolean distinct, int limit) {
        String URL = String.format(dotEnvController.getFromEnv("HOSTNAME") +
                dotEnvController.getFromEnv("DEFAULT") + "/game/scoreboard?distinct=%s", distinct);
        if (limit > 0) {
            URL += String.format("&limit=%s", limit);
        }

        gameScores = new ArrayList<>();
        try {
            HttpResponse<JsonNode> request = Unirest.get(URL).asJson();

            for (Object object : request.getBody().getArray()) {
                JSONObject jsonObject = (JSONObject) object;
                UUID uuid = UUID.fromString(jsonObject.getString("id"));
                String username = jsonObject.getString("username");
                int score = jsonObject.getInt("score");

                gameScores.add(new GameScore(uuid, username, score));
            }
            return gameScores;
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addGameScore(GameScore gameScore) {
        try {
            HttpResponse<JsonNode> jsonResponse =
                    Unirest.post(dotEnvController.getFromEnv("HOSTNAME") +
                                    dotEnvController.getFromEnv("DEFAULT") + "/game/scoreboard")
                            .header("Content-Type", "application/json")
                            .body(gameScore.toJSONObject())
                            .asJson();

        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }


    public ArrayList<GameQuestion> fetchGameQuestions() {
        return fetchGameQuestions(-1);
    }

    public ArrayList<GameQuestion> fetchGameQuestions(int limit) {
        String URL = dotEnvController.getFromEnv("HOSTNAME") +
                dotEnvController.getFromEnv("DEFAULT") + "/game/questions";
        if (limit > 0) {
            URL += String.format("?limit=%s", limit);
        }


        try{
            getGameQuestions(URL);
            return gameQuestions;
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<GameQuestion> fetchGameQuestionsByDepId(UUID departmentId) {
        return fetchGameQuestionsByDepId(departmentId, -1);
    }

    public ArrayList<GameQuestion> fetchGameQuestionsByDepId(UUID departmentId, int limit) {
        if (departmentId == null) {
            return fetchGameQuestions(limit);
        }

        String URL = String.format(dotEnvController.getFromEnv("HOSTNAME") +
                dotEnvController.getFromEnv("DEFAULT") + "/game/questions/%s", departmentId);
        if (limit > 0) {
            URL += String.format("?limit=%s", limit);
        }

        try{
            getGameQuestions(URL);

        }catch(UnirestException e) {
            e.printStackTrace();
        }
        return gameQuestions;
    }

    /**
     * Fills the question array from department "Rijksbreed" if users selected department has less than MAX_QUESTIONS to fill the array.
     * @Author RamonK
     */
    public void fillQuestionArray() {
        int remainingQuestions = MAX_QUESTIONS - gameQuestions.size();
        UUID rijksbreedId = departmentController.fetchDepartmentByDepartmentName("Rijksbreed").getId();
        String URL = String.format(dotEnvController.getFromEnv("HOSTNAME") +
                dotEnvController.getFromEnv("DEFAULT") + "/game/questions/%s?limit=%s",rijksbreedId.toString(), remainingQuestions);
        try{
            getGameQuestions(URL);
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }


    private void getGameQuestions(String URL) throws UnirestException {
        HttpResponse<JsonNode> request = Unirest.get(URL).asJson();
        JSONArray resultList = request.getBody().getArray();

        for (Object object : resultList) {
            JSONObject jsonObject = (JSONObject) object;
            String abbreviationName = jsonObject.getString("abbreviation_name");
            String description = jsonObject.getString("definition");

            gameQuestions.add(new GameQuestion(abbreviationName, description));
        }

        if (gameQuestions.size() < MAX_QUESTIONS) {
            fillQuestionArray();
        }
    }

    public boolean deleteGameScore(JSONObject jsonObject) {
        try{
            HttpResponse<String> jsonNodeHttpResponse =
                    Unirest.delete(dotEnvController.getFromEnv("HOSTNAME") +
                            dotEnvController.getFromEnv("DEFAULT") + "/game")
                            .header("Content-type", "application/json")
                            .body(jsonObject)
                            .asString();
            return jsonNodeHttpResponse.getStatus() == 200;
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return false;
    }
}
