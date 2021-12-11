package nl.hsleiden.AfkoApp.DAO;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import nl.hsleiden.AfkoApp.Controllers.DotEnvController;
import nl.hsleiden.AfkoApp.Models.Report;
import org.json.JSONArray;
import org.json.JSONObject;
import nl.hsleiden.AfkoApp.Models.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * For sending all the report requests.
 * @author Daniel Paans, Ramon Krijnen, Wessel van Leeuwen, Max Mulder
 */
public class ReportDAO {

    private static ReportDAO reportDAO;

    private ArrayList<AbbreviationReport> abbreviationReports;
    private ArrayList<GameScoreReport> gameScoreReports;
    ArrayList<Report> reports;
    DotEnvController dotEnvController;

    public static ReportDAO getInstance() {
        if (reportDAO == null) {
            reportDAO = new ReportDAO();
        }
        return reportDAO;
    }

    private ReportDAO() {
        dotEnvController = DotEnvController.getInstance();
    }

    public void hideGameScoreReport() {

    }

    /**
     * Send request to post a new abbeviation report
     * @author Daniel Paans
     * @param jsonObject
     */
    public void sendAbbreviationReport(JSONObject jsonObject) {
        sendReport(jsonObject, "abbreviation");
    }

    /**
     * Send request to post a new game score report
     * @author Daniel Paans
     * @param jsonObject
     */
    public void sendGameScoreReport(JSONObject jsonObject) {
        sendReport(jsonObject, "gamescore");
    }

    private void sendReport(JSONObject jsonObject, String endpoint) {
        try{
            HttpResponse<JsonNode> jsonNodeHttpResponse =
                    Unirest.post(dotEnvController.getFromEnv("HOSTNAME") +
                                    dotEnvController.getFromEnv("DEFAULT") + "/reports/" + endpoint)
                            .header("Content-type", "application/json")
                            .body(jsonObject)
                            .asJson();

        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send request to remove an abbreviation report.
     * @author Daniel Paans
     * @param abbreviationReport
     */
    public boolean deleteAbbreviationReport(AbbreviationReport abbreviationReport) {
        return deleteReport(abbreviationReport.toJSONObject(), "abbreviation");
    }

    /**
     * Send request to remove a game score report.
     * @author Daniel Paans
     * @param gameScoreReport
     */
    public boolean deleteGameScoreReport(GameScoreReport gameScoreReport) {
        return deleteReport(gameScoreReport.toJSONObject(), "gamescore");
    }

    private boolean deleteReport(JSONObject jsonObject, String endpoint) {
        try{
            HttpResponse<JsonNode> jsonNodeHttpResponse =
                    Unirest.delete(dotEnvController.getFromEnv("HOSTNAME") +
                            dotEnvController.getFromEnv("DEFAULT") + "/reports/"+endpoint)
                            .header("Content-type", "application/json")
                            .body(jsonObject)
                            .asJson();
            return jsonNodeHttpResponse.getStatus() == 200;
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return false;
    }

    //Abbreviations

    /**
     * Sends a request to retrieve all the abbreviation reports.
     * @author Daniel Paans, Max Mulder
     * @return
     */
    public ArrayList<AbbreviationReport> fetchAllAbbreviationReports() {
        try{
            HttpResponse<JsonNode> request = Unirest.get(dotEnvController.getFromEnv("HOSTNAME") +
                            dotEnvController.getFromEnv("DEFAULT") + "/reports/abbreviation/all").asJson();
            JSONArray array = request.getBody().getArray();
            return createAbbreviationReportsOutOfJsonArray(array);
        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private ArrayList<AbbreviationReport> createAbbreviationReportsOutOfJsonArray(JSONArray array) {
        ArrayList<AbbreviationReport> reports = new ArrayList<>();

        for (Object object : array) {
            JSONObject jsonObject = (JSONObject) object;

            UUID id = UUID.fromString(jsonObject.getString("abbreviation_report_id"));
            Abbreviation abbreviation = getAbbreviationOutOfJson(jsonObject.getJSONObject("abbreviation"));
            Timestamp added = Timestamp.valueOf(jsonObject.getString("added").replace('T', ' ').substring(0, 18));
            int ammount = jsonObject.getInt("ammount");
            reports.add(new AbbreviationReport(id, abbreviation, added, ammount));
        }

        return reports;
    }

    private Abbreviation getAbbreviationOutOfJson(JSONObject jsonObject) {

        UUID id = UUID.fromString(jsonObject.getString("id"));
        Set<Department> departments = getDepartmentsOutOfJson(jsonObject.getJSONArray("departments"));
        String name = jsonObject.getString("abbreviation_name");
        String definition = jsonObject.getString("definition");
        Timestamp added = Timestamp.valueOf(jsonObject.getString("added").replace('T', ' ').substring(0, 18));
        boolean hidden = (Boolean) jsonObject.get("hidden");

        return new Abbreviation(id, departments, name, definition, added, hidden);
    }

    private Set<Department> getDepartmentsOutOfJson(JSONArray array) {
        Set<Department> departments = new HashSet<>();

        for(Object object : array) {
            JSONObject jsonObject = (JSONObject) object;

            UUID id = UUID.fromString(jsonObject.getString("id"));
            String depname = jsonObject.getString("department_name");

            departments.add(new Department(depname, id));
        }

        return departments;
    }

    //gamescores

    /**
     * Sends a request to retrieve all the game score reports
     * @author Daniel Paans, Max Mulder
     * @return
     */
    public ArrayList<GameScoreReport> fetchAllGameScoreReports() {
        try{
            HttpResponse<JsonNode> request = Unirest.get(dotEnvController.getFromEnv("HOSTNAME") +
                            dotEnvController.getFromEnv("DEFAULT") + "/reports/gamescore/all").asJson();
            JSONArray array = request.getBody().getArray();
            return createGameScoreReportsOutOfJsonArray(array);
        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private ArrayList<GameScoreReport> createGameScoreReportsOutOfJsonArray(JSONArray array) {
        ArrayList<GameScoreReport> reports = new ArrayList<>();

        for (Object object : array) {
            JSONObject jsonObject = (JSONObject) object;

            UUID id = UUID.fromString(jsonObject.getString("game_score_report_id"));
            GameScore gameScore = getGamescoreOutOfJson(jsonObject.getJSONObject("game_score"));
            Timestamp added = Timestamp.valueOf(jsonObject.getString("added").replace('T', ' ').substring(0, 18));
            int ammount = jsonObject.getInt("ammount");
            reports.add(new GameScoreReport(id, gameScore, added, ammount));
        }

        return reports;
    }

    private GameScore getGamescoreOutOfJson(JSONObject object) {
        JSONObject jsonObject = object;

        UUID id = UUID.fromString(jsonObject.getString("id"));
        String username = jsonObject.getString("username");
        int score = jsonObject.getInt("score");

        return new GameScore(id, username, score);
    }
}
