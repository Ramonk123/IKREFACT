package nl.hsleiden.AfkoApp.DAO;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import nl.hsleiden.AfkoApp.Controllers.DotEnvController;
import nl.hsleiden.AfkoApp.Models.Abbreviation;
import nl.hsleiden.AfkoApp.Models.Department;
import org.json.JSONArray;
import org.json.JSONObject;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class AbbreviationDAO {

    private static AbbreviationDAO abbreviationDAO;

    private ArrayList<Abbreviation> abbreviations;
    DotEnvController dotEnvController;

    /**
     * Creates instance of AbbreviationDAO if it does not exist. Returns Instance.
     * @author InsectByte
     * @return
     */
    public static synchronized AbbreviationDAO getInstance() {
        if (abbreviationDAO == null) {
            abbreviationDAO = new AbbreviationDAO();
        }
        return abbreviationDAO;
    }

    /**
     * Constructor for AbbreviationDAO
     * @author InsectByte
     */
    private AbbreviationDAO()  {
        dotEnvController = DotEnvController.getInstance();
    }

    /**
     * Gets the 10 most recent Abbreviations from the Back-End API
     * @author InsectByte
     * @return ArrayList<Abbreviation>
     */
    public ArrayList<Abbreviation> fetchRecentAbbreviations() {
        try{
            HttpResponse<JsonNode> request = Unirest.get(dotEnvController.getFromEnv("HOSTNAME") +
                    dotEnvController.getFromEnv("DEFAULT") + "/abbreviations/recent").asJson();
            JSONArray array = request.getBody().getArray();
            return createObjectOutOfJsonArray(array);
        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets all abbreviations by keyword and department
     * @author InsectByte
     * @param key
     * @param departmentID
     * @return ArrayList<Abbreviation>
     */
    public ArrayList<Abbreviation> fetchAbbreviationsByKeyword(String key, String departmentID) {
        String URL = dotEnvController.getFromEnv("HOSTNAME") +
                dotEnvController.getFromEnv("DEFAULT") + "/abbreviations/search?keyword=" + key;

        if (departmentID.length() > 0) {URL += "&depid=" + departmentID;}
        try{
            HttpResponse<JsonNode> request = Unirest.get(URL).asJson();
            JSONArray array = request.getBody().getArray();
            return createObjectOutOfJsonArray(array);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Makes Abbreviation Objects out of JSON Array.
     * @author InsectByte
     * @param array
     * @return
     */
    private ArrayList<Abbreviation> createObjectOutOfJsonArray(JSONArray array) {
        abbreviations = new ArrayList<>();

        for (Object object : array) {
            JSONObject jsonObject = (JSONObject) object;

            UUID id = UUID.fromString(jsonObject.getString("id"));
            Set<Department> departments = getDepartmentsOutOfJson(jsonObject.getJSONArray("departments"));
            String name = jsonObject.getString("abbreviation_name");
            String definition = jsonObject.getString("definition");
            Timestamp added = Timestamp.valueOf(jsonObject.getString("added").replace('T', ' ').substring(0, 18));
            boolean hidden = (Boolean) jsonObject.get("hidden");

            abbreviations.add(new Abbreviation(id, departments, name, definition, added, hidden));
        }
        return abbreviations;
    }

    /**
     * Gets departments out of abbreviation object.
     * @author InsectByte
     * @param array
     * @return
     */
    public Set<Department> getDepartmentsOutOfJson(JSONArray array) {
        Set<Department> departments = new HashSet<>();

        for(Object object : array) {
            JSONObject jsonObject = (JSONObject) object;

            UUID id = UUID.fromString(jsonObject.getString("id"));
            String depname = jsonObject.getString("department_name");

            departments.add(new Department(depname, id));
        }

        return departments;
    }

    public void addAbbreviation(Abbreviation abbreviation) {
        try{
            JSONArray body = new JSONArray();
            body.put(abbreviation.toJSONObject());

            HttpResponse<JsonNode> jsonResponse =
                    Unirest.post(dotEnvController.getFromEnv("HOSTNAME") +
                            dotEnvController.getFromEnv("DEFAULT") + "/abbreviations")
                            .header("Content-Type", "application/json")
                            .body(body)
                            .asJson();

        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    public boolean addAbbreviation(String csv) {
        try{
            HttpResponse<JsonNode> jsonResponse =
                    Unirest.post(dotEnvController.getFromEnv("HOSTNAME") +
                            dotEnvController.getFromEnv("DEFAULT") + "/abbreviations/CSV")
                            .header("Content-Type", "text/plain")
                            .body(csv)
                            .asJson();
            return jsonResponse.getStatus() == 200;
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Send request to hide an abbreviation.
     * @author Daniel Paans
     * @param id
     */
    public boolean hideAbbreviation(UUID id) {
        try{
            HttpResponse<JsonNode> jsonResponse =
                    Unirest.put(dotEnvController.getFromEnv("HOSTNAME") +
                            dotEnvController.getFromEnv("DEFAULT") + "/abbreviations/hide/" + id)
                            .header("Content-Type", "text/plain")
                            .asJson();
            return jsonResponse.getStatus() == 200;
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return false;
    }

}
