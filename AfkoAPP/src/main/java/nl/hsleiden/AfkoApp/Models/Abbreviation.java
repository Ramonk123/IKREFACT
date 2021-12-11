package nl.hsleiden.AfkoApp.Models;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class Abbreviation {

    private UUID id;
    private Set<Department> departments;
    private String abbreviation_name;
    private String definition;
    private Timestamp added;
    private boolean hidden;

    public Abbreviation(UUID id, Set<Department> departments, String abbreviation_name, String definition, Timestamp added, boolean hidden) {
        this.id = id;
        this.departments = departments;
        this.abbreviation_name = abbreviation_name;
        this.definition = definition;
        this.added = added;
        this.hidden = hidden;
    }

    public Abbreviation(Set<Department> departments, String abbreviationName, String definition) {
        this.departments = departments;
        this.abbreviation_name = abbreviationName;
        this.definition = definition;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Set<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(Set<Department> departments) {
        this.departments = departments;
    }

    public String getAbbreviation_name() {
        return abbreviation_name;
    }

    public void setAbbreviation_name(String abbreviation_name) {
        this.abbreviation_name = abbreviation_name;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public Timestamp getAdded() {
        return added;
    }

    public void setAdded(Timestamp added) {
        this.added = added;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public JSONObject toJSONObject() {
        JSONObject abbreviationJson = new JSONObject();
        abbreviationJson.put("id", id);
        abbreviationJson.put("abbreviation_name", abbreviation_name);
        abbreviationJson.put("definition", definition);
        abbreviationJson.put("added", added);
        abbreviationJson.put("hidden", hidden);

        ArrayList<JSONObject> departmentsJson = new ArrayList<>();
        for (Department department : departments) {
            departmentsJson.add(department.toJSONObject());
        }
        abbreviationJson.put("departments", departmentsJson);

        return abbreviationJson;
    }
}
