package nl.hsleiden.AfkoApp.Controllers;

import nl.hsleiden.AfkoApp.DAO.ReportDAO;
import nl.hsleiden.AfkoApp.Models.Abbreviation;
import nl.hsleiden.AfkoApp.Models.AbbreviationReport;
import nl.hsleiden.AfkoApp.Models.GameScore;
import nl.hsleiden.AfkoApp.Models.GameScoreReport;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Handles the report requests made by the controllers.
 * @author Ramon Krijnen, Wessel van Leeuwen, Max Mulder, Daniel Paans
 */
public class ReportController {

    static ReportController reportController;
    private AbbreviationController abbreviationController;
    private GameController gameController;

    private ReportDAO reportDAO;

    public static synchronized ReportController getInstance() {
        if (reportController == null) {
            reportController = new ReportController();
        }
        return reportController;
    }

    private ReportController() {
        reportDAO = ReportDAO.getInstance();
        gameController = GameController.getInstance();
        abbreviationController = AbbreviationController.getInstance();
    }

    /**
     * Retrieves all the abbreviation reports.
     * @author Daniel Paans
     * @return
     */
    public ArrayList<AbbreviationReport> getAbbreviationReports() {
        return reportDAO.fetchAllAbbreviationReports();
    }

    /**
     * Retrieves all the game score reports.
     * @author Daniel Paans
     * @return
     */
    public ArrayList<GameScoreReport> getGameScoreReports() {
        return reportDAO.fetchAllGameScoreReports();
    }

    /**
     * Deletes an abbreviation and its reports when the report is accepted by the admin.
     * @author Daniel Paans
     * @param report
     */
    public boolean deleteAbbreviationFromReport(AbbreviationReport report) {
        deleteReport(report);
        return abbreviationController.removeAbbreviation(report.getAbbreviation());
    }

    /**
     * Deletes a gamescore and its reports when the report is accepted by the admin.
     * @author Daniel Paans
     * @param report
     */
    public boolean deleteGameScoreFromReport(GameScoreReport report) {
        deleteReport(report);
        return gameController.removeGameScore(report.getGameScore());
    }

    public void addReport(Abbreviation abbreviation, String description) {
        JSONObject body = new JSONObject();
        body.put("message"," "); //Empty message as it's not implemented in front-end
        body.put("abbreviation",abbreviation.toJSONObject());
        reportDAO.sendAbbreviationReport(body);
    }

    public void addReport(GameScore gameScore, String description) {
        JSONObject body = new JSONObject();
        body.put("message", " ");
        body.put("gameScore", gameScore.toJSONObject());
        reportDAO.sendGameScoreReport(body);
    }

    /**
     * Deletes an abbreviation report.
     * @author Daniel Paans
     * @param abbreviationReport
     */
    public boolean deleteReport(AbbreviationReport abbreviationReport) {
        return reportDAO.deleteAbbreviationReport(abbreviationReport);
    }

    /**
     * Deletes a gamescore report.
     * @author Daniel Paans
     * @param gameScoreReport
     */
    public boolean deleteReport(GameScoreReport gameScoreReport) {
        return reportDAO.deleteGameScoreReport(gameScoreReport);
    }
}
