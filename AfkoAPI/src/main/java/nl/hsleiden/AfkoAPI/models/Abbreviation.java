package nl.hsleiden.AfkoAPI.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

@Entity
@Table
public class Abbreviation {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToMany (cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
        @JoinTable(
                name = "abbreviation_department",
                joinColumns = { @JoinColumn(name = "abbreviationID") },
                inverseJoinColumns = { @JoinColumn(name = "departmentID", updatable = false, insertable = false) }
        )
    private Set<Department> departments;

    private String abbreviation_name;
    private String definition;
    @Column(name = "added")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd hh:mm:sss", timezone="Europe/Berlin")
    private Timestamp added;
    private boolean hidden;

    public Abbreviation(){}

    public Abbreviation(String abbreviation_name, String definition) {
        this.abbreviation_name = abbreviation_name;
        this.definition = definition;
    }

    public Abbreviation(UUID id, Timestamp added, String abbreviation_name, String definition) {
        this.id = id;
        this.added = added;
        this.abbreviation_name = abbreviation_name;
        this.definition = definition;
    }

    public Abbreviation(Set<Department> departments, String abbreviation_name, String definition, Timestamp added) {
        this.departments = departments;
        this.added = added;
        this.abbreviation_name = abbreviation_name;
        this.definition = definition;

    }

    public Abbreviation(UUID id, Set<Department> departments, String abbreviation_name, String definition, Timestamp added) {
        this.id = id;
        this.departments = departments;
        this.abbreviation_name = abbreviation_name;
        this.definition = definition;
        this.added = added;
    }

    public Abbreviation(UUID id, Set<Department> departments, String abbreviation_name, String definition, Timestamp added, boolean hidden) {
        this.id = id;
        this.departments = departments;
        this.abbreviation_name = abbreviation_name;
        this.definition = definition;
        this.added = added;
        this.hidden = hidden;
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
}
