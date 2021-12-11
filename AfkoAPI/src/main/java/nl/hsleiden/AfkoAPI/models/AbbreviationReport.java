package nl.hsleiden.AfkoAPI.models;


import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table
public class AbbreviationReport {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)")
    private UUID abbreviationReportId;

    @ManyToOne
    @JoinColumn(name="abbreviationId", nullable = false)
    private Abbreviation abbreviation;
    private Timestamp added;
    private String message;


    public AbbreviationReport(UUID abbreviationReportId, Abbreviation abbreviation, Timestamp added, String message) {
        this.abbreviationReportId = abbreviationReportId;
        this.abbreviation = abbreviation;
        this.added = added;
        this.message = message;
    }

    public AbbreviationReport() {}

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
