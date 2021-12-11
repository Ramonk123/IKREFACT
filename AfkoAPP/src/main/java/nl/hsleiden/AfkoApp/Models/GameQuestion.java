package nl.hsleiden.AfkoApp.Models;

import org.json.JSONObject;

public class GameQuestion {

    private String abbreviationName;
    private String description;

    public GameQuestion() {}

    public GameQuestion(String abbreviationName, String description) {
        this.abbreviationName = abbreviationName;
        this.description = description;
    }

    public String getAbbreviationName() {
        return abbreviationName;
    }

    public void setAbbreviationName(String abbreviationName) {
        this.abbreviationName = abbreviationName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public JSONObject toJSONObject() {
        JSONObject gameQuestionJson = new JSONObject();
        gameQuestionJson.put("abbreviation_name", abbreviationName);
        gameQuestionJson.put("definition", description);
        return gameQuestionJson;
    }

}
