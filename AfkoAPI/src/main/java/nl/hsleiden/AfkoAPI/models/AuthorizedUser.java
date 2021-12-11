package nl.hsleiden.AfkoAPI.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

/**
 * All the users that authenticate themselves via email.
 * @author Daniel Paans
 */
@Entity
@PrimaryKeyJoinColumn(name = "id")
public class AuthorizedUser extends User{

    private String validationCode;
    private boolean validated;

    public AuthorizedUser() {}

    public AuthorizedUser(String email, String validationCode, boolean validated) {
        super(email);
        this.validationCode = validationCode;
        this.validated = validated;
    }

    public String getValidationCode() {
        return validationCode;
    }

    public void setValidationCode(String validationCode) {
        this.validationCode = validationCode;
    }

    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }
}
