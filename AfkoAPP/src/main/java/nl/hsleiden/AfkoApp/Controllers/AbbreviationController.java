package nl.hsleiden.AfkoApp.Controllers;

import nl.hsleiden.AfkoApp.DAO.AbbreviationDAO;
import nl.hsleiden.AfkoApp.Models.Abbreviation;
import nl.hsleiden.AfkoApp.Models.Department;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Handles the abbreviation requests made by the controllers.
 * @author Ramon Krijnen, Wessel van Leeuwen, Max Mulder, Daniel Paans
 */
public class AbbreviationController {

    private static AbbreviationController abbreviationController;

    private AbbreviationDAO abbreviationDAO;

    /**
     * Creates instance of AbbreviationController if it doesn't exist. Returns instance.
     * @author InsectByte
     * @return AbbreviationController
     */
    public static synchronized AbbreviationController getInstance() {
        if (abbreviationController == null) {
            abbreviationController = new AbbreviationController();
        }
        return abbreviationController;
    }

    /**
     * Constructor for AbbreviationController
     * @author InsectByte
     */
    private AbbreviationController() {
        abbreviationDAO = AbbreviationDAO.getInstance();
    }

    /**
     * Gets the 10 most recent Abbreviations from the AbbreviationDAO
     * @author InsectByte
     * @return ArrayList<Abbreviation>
     */
    public ArrayList<Abbreviation> getRecentAbbreviations() {
        return abbreviationDAO.fetchRecentAbbreviations();
    }

    public ArrayList<Abbreviation> getAbbreviationByKeyword(String key) {
        return abbreviationDAO.fetchAbbreviationsByKeyword(key, "");
    }

    public ArrayList<Abbreviation> getAbbreviationByKeyword(String key, String departmentID) {
        return abbreviationDAO.fetchAbbreviationsByKeyword(key, departmentID);
    }

    public void addAbbreviation(String abbreviationName, String definition, String departmentName) {
        var departmentController = DepartmentController.getInstance();
        Set<Department> departments = new HashSet<>();
        departments.add(departmentController.fetchDepartmentByDepartmentName(departmentName));

        abbreviationDAO.addAbbreviation(new Abbreviation(departments, abbreviationName, definition));
    }

    public boolean addAbbreviation(String csv) {
        return abbreviationDAO.addAbbreviation(csv);
    }

    /**
     * Handles remove abbreviation request from view.
     * @author Daniel Paans
     * @param abbreviation
     */
    public boolean removeAbbreviation(Abbreviation abbreviation) {
        return abbreviationDAO.hideAbbreviation(abbreviation.getId());
    }

}
