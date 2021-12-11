package nl.hsleiden.AfkoApp.DAO;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import nl.hsleiden.AfkoApp.Controllers.DotEnvController;
import nl.hsleiden.AfkoApp.Models.Department;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.*;


public class DepartmentDAO {

    private static DepartmentDAO departmentDAO;

    private ArrayList<Department> departments;
    private HashMap<String, Integer> dpNameIndex;
    private HashMap<UUID, Integer> dpIDIndex;
    DotEnvController dotEnvController;

    /**
     * Creates instance of DepartmentDAO if it does not exist. Returns Instance.
     * @author InsectByte
     * @return DepartmentDAO
     */
    public static synchronized DepartmentDAO getInstance() {
        if (departmentDAO == null) {
            departmentDAO = new DepartmentDAO();
        }
        return departmentDAO;
    }

    /**
     * Constructor for DepartmentDAO
     * @author InsectByte
     */
    private DepartmentDAO() {
        dotEnvController = DotEnvController.getInstance();
    }

    /**
     * Gets all departments from Back-End API
     * @author InsectByte
     * @return ArrayList<Department>
     */
    public ArrayList<Department> fetchAllDepartments() {
        departments = new ArrayList<>();
        dpNameIndex = new HashMap<>();
        dpIDIndex = new HashMap<>();

        try{
            HttpResponse<JsonNode> request = Unirest.get(dotEnvController.getFromEnv("HOSTNAME") +
                    dotEnvController.getFromEnv("DEFAULT") + "/departments").asJson();
            JSONArray array = request.getBody().getArray();

            for (int i = 0; i < array.length(); i++) {
                Object object = array.get(i);

                UUID uuid;
                String depname;

                JSONObject jsonObject = (JSONObject) object;
                uuid = UUID.fromString(jsonObject.getString("id"));
                depname = jsonObject.getString("department_name");

                departments.add(new Department(depname, uuid));
                dpNameIndex.put(depname, i);
                dpIDIndex.put(uuid, i);
            }
            return departments;
        }catch(UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Department fetchDepartmentByDepartmentName(String departmentName) {

        try{
            HttpResponse<JsonNode> request = Unirest.get(dotEnvController.getFromEnv("HOSTNAME") +
                            dotEnvController.getFromEnv("DEFAULT") + "/departments/search")
                    .queryString("departmentName", departmentName)
                    .asJson();
            JSONObject jsonObject = request.getBody().getObject();
            return new Department(jsonObject.getString("department_name"), UUID.fromString(jsonObject.getString("id")));

        } catch (UnirestException unirestException) {
            unirestException.printStackTrace();
        }
        return null;
    }

    public ArrayList<Department> getCachedDepartments() {
        return departments;
    }

    /**
     * Gets a department based on a department name
     * @param department_name
     * @return
     * @author Max Mulder
     */
    public Department getCachedDepartmentByName(String department_name) {

        if (dpNameIndex.containsKey(department_name)) {
            return departments.get(dpNameIndex.get(department_name));
        }
        return null;
    }
}
