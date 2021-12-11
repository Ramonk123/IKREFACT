package nl.hsleiden.AfkoAPI.models;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

/**
 * The role object to assign privileges to users.
 * @author Daniel Paans
 */
@Entity
@Table
public class Role {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;
    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "role")
    private Set<User> users;


    public Role() {}

    public Role(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
