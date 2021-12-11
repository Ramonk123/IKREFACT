package nl.hsleiden.AfkoAPI.models;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;
@Entity
@Table
public class Department {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;
    @Column(unique = true)
    private String department_name;

    public Department() {}

    public Department(String department_name) {
        this.department_name = department_name;
    }

    public Department(UUID id, String department_name) {
        this.id = id;
        this.department_name = department_name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }
}