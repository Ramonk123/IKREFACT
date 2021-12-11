package nl.hsleiden.AfkoApp.Controllers;

import nl.hsleiden.AfkoApp.App;
import nl.hsleiden.AfkoApp.Models.Department;
import nl.hsleiden.AfkoApp.Models.Submit;
import nl.hsleiden.AfkoApp.Observers.SubmitObserver;

import java.util.ArrayList;

/**
 * Handles the getting of departments and adding abbreviations
 * @Author RamonK
 */
public class SubmitController {
    static SubmitController submitController;

    private App app;
    private Submit submit;
    private DepartmentController departmentController;
    private AbbreviationController abbreviationController;

    /**
     * Creates instance of SubmitController if it does not exist. Returns Instance.
     * @author InsectByte
     * @return
     */
    public static synchronized SubmitController getInstance() {
        if (submitController == null) {
            submitController = new SubmitController();
        }
        return submitController;
    }

    /**
     * Loads all necessary dependencies.
     * @author InsectByte
     */
    public void getDependencies() {
        app = App.getInstance();
        departmentController = DepartmentController.getInstance();
        abbreviationController = AbbreviationController.getInstance();
        submit = new Submit();
    }

    /**
     * Constructor for SubmitController.
     * @author InsectByte
     */
    private SubmitController() {
        getDependencies();
    }

    /**
     * Switches Scene on supplied scenename. Makes new Submit Model.
     * @author InsectByte
     * @param scene
     */
    public void switchScene(String scene) {
        app.switchScene(scene);
        submit = new Submit();
    }

    /**
     * Gets all Departments from DepartmentController
     * @author InsectByte
     * @return
     */
    public ArrayList<Department> getDepartments() {
        return departmentController.getDepartments();
    }

    /**
     * Gets all abbreviations by name. Does not filter on department.
     * @author InsectByte
     * @param keyword
     */
    public void getAbbreviations(String keyword) {
        abbreviationController.getAbbreviationByKeyword(keyword);
    }

    /**
     * Implementation of Observer Pattern
     * @author InsectByte
     * @param submitView
     */
    public void registerSubmitObserver(SubmitObserver submitView) {
        this.submit.registerObserver(submitView);
    }

    public void submitAbbreviation(String abbreviationText, String definitionText, String departmentName) {
        abbreviationController.addAbbreviation(abbreviationText, definitionText, departmentName);
    }

    /**
     * Gets all abbreviations by name. Does not fitler on department. Puts them in Submit Model.
     * @author InsectByte
     * @param keyword
     */
    public void setAbbreviationResults(String keyword) {
        submit.setAbbreviationResults(abbreviationController.getAbbreviationByKeyword(keyword));
    }
}