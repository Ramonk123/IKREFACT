package nl.hsleiden.AfkoApp.Models;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * The game score report
 * @author Max Mulder, Daniel Paans
 */
public class GameScoreReport {

    private GameScore gameScore;
    private String firstMessage;
    private UUID gameScoreReportId;
    private Timestamp added;
    private int amount;

    public GameScoreReport() {}

    public GameScoreReport(UUID scoreReportId, GameScore score, Timestamp added, String message, int ammount) {
        this.gameScore = score;
        this.gameScoreReportId = scoreReportId;
        this.added = added;
        this.firstMessage = message;
        this.amount = ammount;
    }

    public GameScoreReport(UUID scoreReportId, GameScore score, Timestamp added, int ammount) {
        this.gameScore = score;
        this.gameScoreReportId = scoreReportId;
        this.added = added;
        this.amount = ammount;
    }

    public GameScore getGameScore() {
        return gameScore;
    }

    public void setGameScore(GameScore gameScore) {
        this.gameScore = gameScore;
    }

    public UUID getGameScoreReportId() {
        return gameScoreReportId;
    }

    public void setGameScoreReportId(UUID scoreReportId) {
        this.gameScoreReportId = scoreReportId;
    }

    public String getFirstMessage() {
        return firstMessage;
    }

    public void setFirstMessage(String firstMessage) {
        this.firstMessage = firstMessage;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Timestamp getAdded() {
        return added;
    }

    public void setAdded(Timestamp added) {
        this.added = added;
    }

    public JSONObject toJSONObject() {
        JSONObject gameScoreReportJson = new JSONObject();
        gameScoreReportJson.put("gameScore", gameScore.toJSONObject());
        gameScoreReportJson.put("message", firstMessage);
        gameScoreReportJson.put("added", added.toString().replace(" ", "T"));
        gameScoreReportJson.put("gameScoreReportId", gameScoreReportId);
        return gameScoreReportJson;
    }
}
