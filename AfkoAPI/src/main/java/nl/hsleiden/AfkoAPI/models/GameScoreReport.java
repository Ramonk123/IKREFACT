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

//    @ManyToOne
//    @JoinColumn(nullable = false)
    @Column(columnDefinition = "BINARY(16)")
    private UUID gameScoreId;
    private String message;
    private Timestamp added;

    public GameScoreReport() {}

    public GameScoreReport(UUID score, UUID scoreReportId, String message, Timestamp added) {
        this.gameScoreId = score;
        this.gameScoreReportId = scoreReportId;
        this.message = message;
        this.added = added;
    }

    public UUID getGameScoreId() {
        return gameScoreId;
    }

    public void setGameScore(UUID gameScore) {
        this.gameScoreId = gameScore;
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
