package nl.hsleiden.AfkoApp.Controllers;


import javafx.stage.FileChooser;
import javafx.stage.Stage;
import nl.hsleiden.AfkoApp.Models.*;
import nl.hsleiden.AfkoApp.Observers.AdminObserver;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static nl.hsleiden.AfkoApp.App.app;

/**
 * Handles the admin requests made by the view.
 * @author Wessel van Leeuwen, Max Mulder, Daniel Paans
 */
public class AdminController {

    private ReportController reportController;
    private AbbreviationController abbreviationController;
    private AuthenticationController authenticationController;

    private static AdminController adminController;

    private Admin admin;
    private AdminPanel adminPanel;

    public static AdminController getInstance() {
        if (adminController == null) {
            adminController = new AdminController();
        }
        return adminController;
    }

    private void getDependencies() {
        this.reportController = ReportController.getInstance();
        this.abbreviationController = AbbreviationController.getInstance();
        this.authenticationController = AuthenticationController.getInstance();
    }

    private AdminController() {
        adminPanel = new AdminPanel();
        getDependencies();
    }

    public void registerAdminObserver(AdminObserver adminPanelView) {
        this.adminPanel.registerObserver(adminPanelView);
    }

    public void getAbbreviationReports() {
        adminPanel.setAbbreviationReports(reportController.getAbbreviationReports());
    }

    public void getGameScoreReports() {
        adminPanel.setGameScoreReports(reportController.getGameScoreReports());
    }

    public boolean deleteAbbreviationReport(AbbreviationReport abbreviationReport) {
        if(reportController.deleteReport(abbreviationReport)) {
            updateReports();
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteGameScoreReport(GameScoreReport gameScoreReport) {
        if (reportController.deleteReport(gameScoreReport)) {
            updateReports();
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteAbbreviationByReport(AbbreviationReport report) {
        if (reportController.deleteAbbreviationFromReport(report)) {
            updateReports();
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteGameScoreByReport(GameScoreReport report) {
        if (reportController.deleteGameScoreFromReport(report)) {
            updateReports();
            return true;
        } else {
            return false;
        }
    }

    public boolean uploadCSV(String csv) {
        return abbreviationController.addAbbreviation(csv);
    }

    public void login(String username, String password) {
        admin = new Admin(authenticationController.authenticate(username, password));
    }

    /**
     * Returns current JWT token in use
     * @author InsectByte
     * @return String
     */
    public String getJWT () {
        return admin.getJWTToken();
    }

    /**
     * Switches seen based on supplied param
     * @author InsectByte
     * @param scene
     */
    public void switchScene(String scene) {
        app.switchScene(scene);
        Admin admin = new Admin();
    }

    public String OpenCSV() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open CSV-bestand");
        return openTextFile(fileChooser.showOpenDialog(new Stage()));

    }

    public String openTextFile(File file) throws IOException {
        FileReader reader = new FileReader(file);
        String data = "";
        int character;
        while ((character = reader.read()) != -1) {
            data += (char) character;
        }
        return data;
    }

    private void updateReports() {
        adminPanel.setAbbreviationReports(reportController.getAbbreviationReports());
        adminPanel.setGameScoreReports(reportController.getGameScoreReports());
    }
}
