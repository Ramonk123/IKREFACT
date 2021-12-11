package nl.hsleiden.AfkoApp.Models;

import org.json.JSONObject;

import java.util.UUID;

public class Department {
    String depname;
    UUID id;

    /**
     * Constructor for Department
     * @author InsectByte
     * @param depname
     * @param id
     */
    public Department(String depname, UUID id) {
        this.depname = depname;
        this.id = id;
    }

    /**
     * Gets name from department Object.
     * @author InsectByte
     * @return String
     */
    public String getDepname() {
        return depname;
    }

    /**
     * Sets department name from supplied name.
     * @author InsectByte
     * @param depname
     */
    public void setDepname(String depname) {
        this.depname = depname;
    }

    /**
     * Gets the department's UUID
     * @author InsectByte
     * @return UUID
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets department's UUID from supplied ID
     * @param id
     */
    public void setId(UUID id) {
        this.id = id;
    }

    public JSONObject toJSONObject() {
        JSONObject departmentJson = new JSONObject();
        departmentJson.put("department_name", depname);
        departmentJson.put("id", id);
        return departmentJson;
    }
}
