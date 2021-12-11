package nl.hsleiden.AfkoAPI.models;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table
public class GameScoreReport {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)")
    private UUID gameScoreReportId;

    @ManyToOne
    @JoinColumn(nullable = false)
    private GameScore gameScore;
    private String message;
    private Timestamp added;

    public GameScoreReport() {}

    public GameScoreReport(GameScore score, UUID scoreReportId, String message, Timestamp added) {
        this.gameScore = score;
        this.gameScoreReportId = scoreReportId;
        this.message = message;
        this.added = added;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getAdded() {
        return added;
    }

    public void setAdded(Timestamp added) {
        this.added = added;
    }
}
