package nl.hsleiden.AfkoAPI.models;

import java.util.UUID;

/**
 * The response for authentication
 * @author Daniel Paans
 */
public class AuthenticationResponse {

    private final UUID ID;
    private final Role ROLE;
    private final String JWT;

    public AuthenticationResponse(UUID id, Role role, String jwt) {
        this.ID = id;
        this.ROLE = role;
        this.JWT = jwt;
    }

    public Role getROLE() {
        return ROLE;
    }

    public UUID getID() {
        return ID;
    }

    public String getJWT() {
        return JWT;
    }
}
