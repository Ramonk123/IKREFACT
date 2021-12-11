package nl.hsleiden.AfkoAPI.dao;

import nl.hsleiden.AfkoAPI.models.AbbreviationReport;
import nl.hsleiden.AfkoAPI.models.GameScoreReport;
import nl.hsleiden.AfkoAPI.repositories.AbbreviationReportCountRepository;
import nl.hsleiden.AfkoAPI.repositories.AbbreviationReportRepository;
import nl.hsleiden.AfkoAPI.repositories.GameScoreReportCountRepository;
import nl.hsleiden.AfkoAPI.repositories.GameScoreReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.sql.Date;
import java.util.*;

/**
 * Removes and adds reports of gameScores and abbreviations in the database.
 * @author Daniel Paans, Max Mulder
 */
@Service
public class ReportDAO {

    private final AbbreviationReportRepository ABBREVIATION_REPORT_REPOSITORY;
    private final AbbreviationReportCountRepository ABBREVIATION_REPORT_COUNT_REPOSITORY;

    private final GameScoreReportRepository GAME_SCORE_REPORT_REPOSITORY;
    private final GameScoreReportCountRepository GAME_SCORE_REPORT_COUNT_REPOSITORY;

    @Autowired
    public ReportDAO(AbbreviationReportRepository abbreviationReportRepository, AbbreviationReportCountRepository abbreviationReportCountRepository, GameScoreReportRepository gameScoreReportRepository, GameScoreReportCountRepository gameScoreReportCountRepository) {
        this.ABBREVIATION_REPORT_REPOSITORY = abbreviationReportRepository;
        this.ABBREVIATION_REPORT_COUNT_REPOSITORY = abbreviationReportCountRepository;

        this.GAME_SCORE_REPORT_REPOSITORY = gameScoreReportRepository;
        this.GAME_SCORE_REPORT_COUNT_REPOSITORY = gameScoreReportCountRepository;
    }

    // GameScoreReports

    /**
     * Retrieves all the abbreviationReports from the database.
     * @author Max Mulder
     * @return
     */
    public List<Map<String, Object>> getAbbreviationReports() {
        return ABBREVIATION_REPORT_COUNT_REPOSITORY.getReports();
    }

    /**
     * Adds an abbreviationReport to the database.
     * @author Daniel Paans
     * @param abbreviationReport
     * @return
     */
    public AbbreviationReport postAbbreviationReport(AbbreviationReport abbreviationReport) {
        return ABBREVIATION_REPORT_REPOSITORY.save(abbreviationReport);
    }

    /**
     * Removes an abbreviation to the database.
     * @author Daniel Paans
     * @param abbreviationReport
     * @return
     */
    public String removeAbbreviationReport(AbbreviationReport abbreviationReport) {
        String abbreviationId = abbreviationReport.getAbbreviationReportId().toString();
        String reportedAbbreviation = abbreviationReport.getAbbreviation().getAbbreviation_name();

        ABBREVIATION_REPORT_REPOSITORY.deleteAllReportsByAbbreviationId(abbreviationReport.getAbbreviation().getId());
        return String.format("Abbreviation report %s with abbreviation %s was deleted from the database", abbreviationId, reportedAbbreviation);
    }

    // GameScoreReports

    /**
     * Retrieves the gamescoreReports from the database.
     * @author Max Mulder
     * @return
     */
    public List<Map<String, Object>> getGameScoreReports() {
        return GAME_SCORE_REPORT_COUNT_REPOSITORY.getReports();
    }

    /**
     * Adds a gameScoreReport to the database.
     * @author Daniel Paans
     * @param gameScoreReport
     * @return
     */
    public GameScoreReport postGameScoreReport(GameScoreReport gameScoreReport) {
        return GAME_SCORE_REPORT_REPOSITORY.save(gameScoreReport);
    }

    /**
     * Removes a gameScoreReport from the database.
     * @autor Daniel Paans
     * @param gameScoreReport
     * @return
     */
    public String removeGameScoreReport(GameScoreReport gameScoreReport) {
        String reportId = gameScoreReport.getGameScoreReportId().toString();
        String reportedUsername = gameScoreReport.getGameScore().getUsername();

        GAME_SCORE_REPORT_REPOSITORY.deleteAllReportsByGameScoreId(gameScoreReport.getGameScore().getId());
        return String.format("Gamescore report %s with username %s was deleted from the database", reportId, reportedUsername);
    }
}
