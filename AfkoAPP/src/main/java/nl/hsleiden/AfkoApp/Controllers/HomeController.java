package nl.hsleiden.AfkoApp.Controllers;

import nl.hsleiden.AfkoApp.App;
import nl.hsleiden.AfkoApp.Models.Abbreviation;
import nl.hsleiden.AfkoApp.Models.Department;
import nl.hsleiden.AfkoApp.Models.Home;
import nl.hsleiden.AfkoApp.Observers.HomeObserver;

import java.util.ArrayList;

public class HomeController {

    static HomeController homeController;

    private App app;
    private Home home;
    private DepartmentController departmentController;
    private AbbreviationController abbreviationController;

    /**
     * Creates instance of HomeController if it does not exist. Returns instance.
     * @author InsectByte
     * @return HomeController
     */
    public static synchronized HomeController getInstance() {
        if (homeController == null) {
            homeController = new HomeController();
        }
        return homeController;
    }

    /**
     * Loads all necessary controllers
     * @author InsectByte
     */
    void getDependencies() {
        app = App.getInstance();
        departmentController = DepartmentController.getInstance();
        abbreviationController = AbbreviationController.getInstance();
        home = new Home();
    }

    /**
     * Constructor for HomeController
     * @author InsectByte
     */
    private HomeController() {
        getDependencies();
    }

    /**
     * Switches scene on supplied scenename. Makes New Homemodel.
     * @param scene
     */
    public void switchScene(String scene) {
        app.switchScene(scene);
        home = new Home();
    }

    /**
     * Implmentation of Observer Pattern
     * @author InsectByte
     * @param homeView
     */
    public void registerHomeObserver(HomeObserver homeView) {
        this.home.registerObserver(homeView);
    }

    /**
     * Gets all Departments from DepartmentController.
     * @author InsectByte
     * @return ArrayList<Department>
     */
    public ArrayList<Department> getDepartments() {
        return departmentController.getDepartments();
    }

    /**
     * Gets 10 most recent abbreviations from AbbreviationController. Puts them in Home Model.
     * @author InsectByte
     */
    public void getRecents() {
        home.setRecentAbbreviations(abbreviationController.getRecentAbbreviations());
    }

    /**
     * Gets abbreviations by abbreviation name and optionally by department.
     * @author InsectByte
     * @param key
     * @param departmentName
     */
    public void getResults(String key, String departmentName) {
        if (departmentName.equals("Allemaal")) {
            home.setResultAbbreviations(abbreviationController.getAbbreviationByKeyword(key, ""));
        } else {
            String departmentID = departmentController.fetchDepartmentByDepartmentName(departmentName).getId().toString();
            home.setResultAbbreviations(abbreviationController.getAbbreviationByKeyword(key, departmentID));
        }
    }
}
