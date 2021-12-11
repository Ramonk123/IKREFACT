package nl.hsleiden.AfkoAPI.controllers;

import nl.hsleiden.AfkoAPI.dao.AbbreviationDAO;
import nl.hsleiden.AfkoAPI.exceptions.*;
import nl.hsleiden.AfkoAPI.httpResponses.Response;
import nl.hsleiden.AfkoAPI.models.Abbreviation;
import nl.hsleiden.AfkoAPI.services.CSVParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Handles abbreviation requests in the backend
 * @author Ramon Krijnen, Max Mulder, Daniel Paans
 */
@RestController
@RequestMapping("${DEFAULT_PATH}${ABBREVIATIONS}")
@CrossOrigin(origins = "http://localhost:4200")
public class AbbreviationController {

    private final AbbreviationDAO ABBREVIATION_DAO;
    private final CSVParser CSV_PARSER;

    @Autowired
    public AbbreviationController(AbbreviationDAO abbreviationDAO,CSVParser csvParser) {
        this.ABBREVIATION_DAO = abbreviationDAO;
        this.CSV_PARSER = csvParser;
    }

    /**
     * Retrieves all the abbreviations
     * @author Ramon Krijnen
     * @param departmentID
     * @return
     */
    @GetMapping
    public List<Abbreviation> getAbbreviations(@RequestParam(value = "depID") Optional<String> departmentID) {
        if (departmentID.isPresent()) {return ABBREVIATION_DAO.findAbbreviationByDepartmentId(departmentID.get());}
        return ABBREVIATION_DAO.getAbbreviations();
    }

    /**
     * finds abbreviation by keyword
     * @author Max Mulder
     * @param depid
     * @param keyword
     * @return
     */
    @GetMapping("/search")
    public Optional<List<Abbreviation>> findAbbreviationsByAbbreviation_name(
            @RequestParam(value = "depid") Optional<String> depid,
            @RequestParam(value = "keyword") String keyword) {

        if (depid.isPresent()) {return ABBREVIATION_DAO.findAbbreviationsByAbbreviation_name(keyword, depid.get());}
        return ABBREVIATION_DAO.findAbbreviationsByAbbreviation_name(keyword);
    }

    /**
     * Retrieves all abbreviations by category
     * @author Ramon Krijnen
     * @param value1
     * @param value2
     * @return
     */
    @GetMapping("/search/category")
    public Optional<List<Abbreviation>> findAbbreviationsByCategory(@RequestParam("value1") String value1,
                                                                    @RequestParam("value2") String value2){
        return ABBREVIATION_DAO.findAbbreviationsByCategory(value1,value2);
    }

    /**
     * Retrieves all recently added abbreviations
     * @author Ramon Krijnen
     * @return
     */
    @GetMapping("/recent")
    public List<Abbreviation> getRecent() {
        return ABBREVIATION_DAO.getRecent();
    }

    /**
     * Edits an abbreviation name and definition
     * @author Ramon Krijnen
     * @param id
     * @param abbreviationName
     * @param definition
     * @return
     */
    @PutMapping("/{id}")
    //@Secured("ROLE_ADMIN")
    public ResponseEntity<Response> editAbbreviation(@PathVariable UUID id, @RequestParam("abbreviationName") String abbreviationName,
                                           @RequestParam("definition") String definition){
         ABBREVIATION_DAO.editAbbreviation(id, abbreviationName, definition);
         return ResponseEntity.ok(new Response(String.format("New abbreviation %s (%s) with definition: %s", abbreviationName, id, definition)));
    }

    /**
     * Adds an abbreviation in the database.
     * @author Ramon Krijnen
     * @param abbreviations
     * @return
     */
    @PostMapping
//    @Secured({"ROLE_AUTHORIZED_USER", "ROLE_ADMIN"})
    public List<Abbreviation> postAbbreviations(@RequestBody List<Abbreviation> abbreviations) {
        return ABBREVIATION_DAO.postAbbreviations(abbreviations);
    }

    /**
     * @author Daniel Paans
     * @param id
     * @return
     */
    @PutMapping("/hide/{id}")
    public ResponseEntity<Response> removeAbbreviation(@PathVariable UUID id) {
        ABBREVIATION_DAO.hideAbbreviation(id);
        return ResponseEntity.ok(new Response("removed abbreviation"));
    }


    /**
    * Converts CSV to Abbreviation models and adds them to the database.
     * @author Max Mulder
    * @param abbreviationCsv A csv in text form that is sent with the http request.
    * @param separator The separator used when paring the columns of the csv. Default: ","
    * @param depSeparator The separator used when parsing the departments from the csv. Default: ";"
    * @return List of added Abbreviations
    */
    @PostMapping("/CSV")
//    @Secured("ROLE_ADMIN")
    public List<Abbreviation> postAbbreviations(@RequestBody Optional<String> abbreviationCsv,
                                                @RequestParam("separator") Optional<String> separator,
                                                @RequestParam("depSeparator") Optional<String> depSeparator)
            throws CSVException, DepartmentDoesntExistException {
        if (abbreviationCsv.isEmpty()) {throw new CSVNotFoundException();}
        if (separator.isEmpty()) {separator = Optional.of(",");}
        if (depSeparator.isEmpty()) {depSeparator = Optional.of(";");}

        return ABBREVIATION_DAO.postAbbreviations(CSV_PARSER.Parse(abbreviationCsv.get(), separator.get(), depSeparator.get()));
    }
}
