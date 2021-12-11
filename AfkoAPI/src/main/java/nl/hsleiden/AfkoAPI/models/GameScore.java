package nl.hsleiden.AfkoAPI.models;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "gamescore")
public class GameScore {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private String username;
    private int score;
    @Column(columnDefinition = "BINARY(16)")
    private UUID depID;

    public GameScore(){}

    public GameScore(UUID id, String username, int score) {
        this.id = id;
        this.username = username;
        this.score = score;
    }

    public GameScore(UUID id, String username, int score, UUID depID) {
        this.id = id;
        this.username = username;
        this.score = score;
        this.depID = depID;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public UUID getDepID() {
        return depID;
    }

    public void setDepID(UUID depID) {
        this.depID = depID;
    }
}
