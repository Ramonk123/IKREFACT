package nl.hsleiden.AfkoAPI.exceptions;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Thrown when the given role is not found in the database.
 * @author Daniel Paans
 */
public class IncorrectRoleException extends UsernameNotFoundException {
    public IncorrectRoleException(String msg) {
        super(msg);
    }
}
