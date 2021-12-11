package nl.hsleiden.AfkoAPI.dao;

import nl.hsleiden.AfkoAPI.models.Abbreviation;
import nl.hsleiden.AfkoAPI.repositories.AbbreviationRepository;
import nl.hsleiden.AfkoAPI.services.CSVParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.*;

@Service
public class AbbreviationDAO {

    private final AbbreviationRepository ABBREVIATION_REPOSITORY;

    @Autowired
    public AbbreviationDAO(AbbreviationRepository abbreviationRepository) {
        this.ABBREVIATION_REPOSITORY = abbreviationRepository;
    }

    public List<Abbreviation> getAbbreviations() {
        return ABBREVIATION_REPOSITORY.findAll();
    }

    public List<Abbreviation> getRecent() {
        return ABBREVIATION_REPOSITORY.getRecent();
    }

    public Optional<List<Abbreviation>> findAbbreviationsByAbbreviation_name(String keyword) {
        return ABBREVIATION_REPOSITORY.findAbbreviationsByAbbreviation_name(keyword);
    }

    /**
     * gets abbreviation by name
     * @author Max Mulder
     * @param keyword
     * @param depid
     * @return
     */
    public Optional<List<Abbreviation>> findAbbreviationsByAbbreviation_name(String keyword, String depid) {
        return ABBREVIATION_REPOSITORY.findAbbreviationsByAbbreviation_name(keyword, depid);
    }

    public Optional<List<Abbreviation>> findAbbreviationsByCategory(String value1, String value2){
        return ABBREVIATION_REPOSITORY.findAbbreviationsByCategory(value1, value2);
    }

    public Optional<Abbreviation> findAbbreviationById(UUID id) {
        return ABBREVIATION_REPOSITORY.findAbbreviationById(id);
    }

    /**
     * gets abbreviations by department id
     * @author Max Mulder
     * @param id
     * @return
     */
    public List<Abbreviation> findAbbreviationByDepartmentId(String id) {
        return ABBREVIATION_REPOSITORY.findAbbreviationsByDepartmentId(id);
    }

    /**
     * Sets the hide attribute to true in the database.
     * @author Daniel Paans
     * @param id
     */
    public void hideAbbreviation(UUID id) {
        ABBREVIATION_REPOSITORY.hideAbbreviation(id);
    }

    /**
     * Adds a list of Abbreviation models to the database.
     * @param abbreviations A list of Abbreviation models
     * @return List of added Abbreviations
     */
    public List<Abbreviation> postAbbreviations(List<Abbreviation> abbreviations) {

        for (Abbreviation abbreviation: abbreviations) {
            abbreviation.setAdded(new Timestamp(Calendar.getInstance().getTime().getTime()));
        }

        return ABBREVIATION_REPOSITORY.saveAll(abbreviations);
    }

    public void editAbbreviation(UUID id, String abbreviationName, String definition) {
        ABBREVIATION_REPOSITORY.editAbbreviation(id, abbreviationName, definition);
    }
}
