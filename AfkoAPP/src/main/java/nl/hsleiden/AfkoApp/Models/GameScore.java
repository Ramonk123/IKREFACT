package nl.hsleiden.AfkoApp.Models;

import org.json.JSONObject;

import java.util.UUID;

public class GameScore {

    private UUID id;
    private String username;
    private int score;

    public GameScore(){}

    public GameScore(UUID id, String username, int score){
        this.id = id;
        this.username = username;
        this.score = score;
    }

    public GameScore(String username, int score){
        this.username = username;
        this.score = score;
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

    public JSONObject toJSONObject() {
        JSONObject gameScoreJson = new JSONObject();
        gameScoreJson.put("username", username);
        gameScoreJson.put("score", score);
        gameScoreJson.put("id", id);
        return gameScoreJson;
    }
}
