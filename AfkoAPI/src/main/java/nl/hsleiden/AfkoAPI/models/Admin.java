package nl.hsleiden.AfkoAPI.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * The admin object.
 * @author Daniel Paans
 */
@Entity
@PrimaryKeyJoinColumn(name = "id")
public class Admin extends User {

    @Column(unique = true)
    private String username;
    @Column(unique = true, length = 60)
    private String password;

    public Admin() {};

    public Admin(String email, String username, String password) {
        super(email);
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
