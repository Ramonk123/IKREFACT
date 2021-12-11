package nl.hsleiden.AfkoApp.Models;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * The abbreviation report
 * @author Max Mulder, Daniel Paans
 */
public class AbbreviationReport {

    private UUID abbreviationReportId;
    private Abbreviation abbreviation;
    private Timestamp added;
    private String firstMessage;
    private int ammount;

    public AbbreviationReport() {}

    public AbbreviationReport(UUID abbreviationReportId, Abbreviation abbreviation, Timestamp added, String message, int ammount) {
        this.abbreviationReportId = abbreviationReportId;
        this.abbreviation = abbreviation;
        this.added = added;
        this.firstMessage = message;
        this.ammount = ammount;
    }

    public AbbreviationReport(UUID id, Abbreviation abbreviation, Timestamp added, int ammount) {
        this.abbreviationReportId = id;
        this.abbreviation = abbreviation;
        this.added = added;
        this.ammount = ammount;
    }

    public UUID getAbbreviationReportId() {
        return abbreviationReportId;
    }

    public void setAbbreviationReportId(UUID abbreviationReportId) {
        this.abbreviationReportId = abbreviationReportId;
    }

    public Abbreviation getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(Abbreviation abbreviation) {
        this.abbreviation = abbreviation;
    }

    public Timestamp getAdded() {
        return added;
    }

    public void setAdded(Timestamp added) {
        this.added = added;
    }

    public String getFirstMessage() {
        return firstMessage;
    }

    public void setFirstMessage(String firstMessage) {
        this.firstMessage = firstMessage;
    }

    public int getAmmount() {
        return ammount;
    }

    public void setAmmount(int ammount) {
        this.ammount = ammount;
    }

    public JSONObject toJSONObject() {
        JSONObject reportJson = new JSONObject();
        reportJson.put("message", " ");
        reportJson.put("abbreviationReportId", abbreviationReportId);
        reportJson.put("abbreviation", abbreviation.toJSONObject());
        reportJson.put("added", added.toString().replace(" ", "T"));
        return reportJson;
    }
}
