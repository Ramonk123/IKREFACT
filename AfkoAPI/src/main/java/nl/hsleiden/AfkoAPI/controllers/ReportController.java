package nl.hsleiden.AfkoAPI.controllers;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import nl.hsleiden.AfkoAPI.dao.AbbreviationDAO;
import nl.hsleiden.AfkoAPI.dao.GameDAO;
import nl.hsleiden.AfkoAPI.dao.ReportDAO;
import nl.hsleiden.AfkoAPI.exceptions.IncorrectBodyException;
import nl.hsleiden.AfkoAPI.httpResponses.Response;
import nl.hsleiden.AfkoAPI.models.Abbreviation;
import nl.hsleiden.AfkoAPI.models.AbbreviationReport;
import nl.hsleiden.AfkoAPI.models.GameScore;
import nl.hsleiden.AfkoAPI.models.GameScoreReport;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.*;

/**
 * Handles all the report requests: it deletes, adds and retrieves reports of abbreviations and gamescores.
 * @author Daniel Paans
 */
@RestController
@Secured({"ROLE_AUTHORIZED_USER"})
@RequestMapping("${DEFAULT_PATH}${REPORT}")
@CrossOrigin(origins = "http://localhost:4200")
public class ReportController {

    private final ReportDAO REPORT_DAO;
    private final AbbreviationDAO ABBREVIATION_DAO;
    private final GameDAO GAME_DAO;

    @Autowired
    public ReportController(ReportDAO reportDAO, AbbreviationDAO abbreviationDAO, GameDAO gameDAO) {
        this.REPORT_DAO = reportDAO;
        this.ABBREVIATION_DAO = abbreviationDAO;
        this.GAME_DAO = gameDAO;
    }

    /**
     * gets abbreviationReports from the database
     * @author MaxMulder
     * @return
     */
    @GetMapping("/abbreviation")
    public List<HashMap<String, Object>> getAbbreviationReports() {
        // casting the tuple map to a hashmap
        List<HashMap<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> map : REPORT_DAO.getAbbreviationReports()) {
            HashMap<String, Object> entry = new HashMap<>();
            for (String key : map.keySet()) {
                entry.put(key, map.get(key));
            }
            entry.put("abbreviation", ABBREVIATION_DAO.findAbbreviationById(UUID.fromString((String) entry.get("abbreviation_id"))));
            entry.remove("abbreviation_id");
            result.add(entry);
        }
        return result;
    }

    /**
     * Creates a new abbreviation report in the database.
     * @author Daniel Paans
     * @param abbreviationReport
     * @return
     */
    @PostMapping("/abbreviation")
    public AbbreviationReport postAbbreviationReport(@RequestBody AbbreviationReport abbreviationReport) {
        abbreviationReport.setAdded(new Timestamp(Calendar.getInstance().getTime().getTime()));
        return REPORT_DAO.postAbbreviationReport(abbreviationReport);
    }

    /**
     * Removes an abbreviationReport from the database.
     * @author Daniel Paans
     * @param abbreviationReport
     * @return
     * @throws IncorrectBodyException
     */
    @DeleteMapping("/abbreviation")
//    @Secured("ROLE_ADMIN")
    public ResponseEntity<Response> deleteAbbreviationReport(@RequestBody AbbreviationReport abbreviationReport) throws IncorrectBodyException {
        try {

            REPORT_DAO.removeAbbreviationReport(abbreviationReport);
            Abbreviation abbreviation = abbreviationReport.getAbbreviation();
            String responseMessage = String.format("Deleted gamescore report %s with gamescore: %s, %s", abbreviationReport.getAbbreviationReportId(), abbreviation.getAbbreviation_name(), abbreviation.getDefinition());
            return ResponseEntity.ok(new Response(responseMessage));
        } catch(NullPointerException npe) {
            throw new IncorrectBodyException();
        }
    }

    /**
     * Gets gameScoreReports from the database
     * @author Max Mulder
     * @return
     */
    @GetMapping("/gamescore")
    public List<HashMap<String, Object>> getGameScoreReports() {
        // casting the tuple map to a hashmap
        List<HashMap<String, Object>> result = new ArrayList<>();

        for (Map<String, Object> map : REPORT_DAO.getGameScoreReports()) {
            HashMap<String, Object> entry = new HashMap<>();
            for (String key : map.keySet()) {
                entry.put(key, map.get(key));
            }

            // Code smell?
            String URL = "http://localhost:8444/scoreboard/";
            try {
                HttpResponse<JsonNode> request = Unirest.get(URL+entry.get("game_score_id")).asJson();
                JSONObject jsonObject = request.getBody().getObject();

                GameScore gameScore = new GameScore(
                        UUID.fromString(jsonObject.getString("id")),
                        jsonObject.getString("username"),
                        jsonObject.getInt("score")
                );
                entry.put("game_score", gameScore);
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            entry.remove("game_score_id");

            result.add(entry);

        }
        return result;
    }

    /**
     * Creates a new GamescoreReport
     * @author Daniel Paans
     * @param gameScoreReport
     * @return
     */
    @PostMapping("/gamescore")
    public GameScoreReport postGameScoreReport(@RequestBody GameScoreReport gameScoreReport) {
        gameScoreReport.setAdded(new Timestamp(Calendar.getInstance().getTime().getTime()));
        return REPORT_DAO.postGameScoreReport(gameScoreReport);
    }

    /**
     * Removes a GamescoreReport from the database.
     * @author Daniel Paans
     * @param gameScoreReport
     * @return
     * @throws IncorrectBodyException
     */
    @DeleteMapping("/gamescore")
//    @Secured("ROLE_ADMIN")
    public ResponseEntity<Response> deleteGameScoreReport(@RequestBody GameScoreReport gameScoreReport) throws IncorrectBodyException {
        try {
            REPORT_DAO.removeGameScoreReport(gameScoreReport);

            UUID gameScoreId = gameScoreReport.getGameScoreId();
            String responseMessage = String.format("Deleted gamescore report %s with gamescoreid: %s", gameScoreReport.getGameScoreReportId(), gameScoreId);
            return ResponseEntity.ok(new Response(responseMessage));
        } catch(NullPointerException npe) {
            throw new IncorrectBodyException();
        }
    }
}
