package nl.hsleiden.AfkoAPI.models;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

/**
 * The parent object of users.
 * @author Daniel Paans
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table
public class User {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false)
    private String email;

    @ManyToOne
    @JoinColumn(name="role_id", nullable = false)
    private Role role;

    public User() {}

    public User(String email) {
        this.email = email;
    }

    public User(String email, Role role) {
        this(email);
        this.role = role;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }


}
