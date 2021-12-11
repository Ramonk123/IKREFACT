package nl.hsleiden.AfkoApp.Models;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.UUID;

public class Report {
    private UUID id;
    private Abbreviation abbreviation;
    private Timestamp added;
    private String description;

    public Report(UUID id, Abbreviation abbreviation, Timestamp added, String description) {
        this.id = id;
        this.abbreviation = abbreviation;
        this.added = added;
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
