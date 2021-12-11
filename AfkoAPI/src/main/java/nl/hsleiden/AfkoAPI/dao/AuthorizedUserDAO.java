package nl.hsleiden.AfkoAPI.dao;

import nl.hsleiden.AfkoAPI.models.AuthorizedUser;
import nl.hsleiden.AfkoAPI.repositories.AuthorizedUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

/**
 * Stores and removes an AuthorizedUser from the database.
 * @author Daniel Paans
 */
@Service
public class AuthorizedUserDAO {

    private final AuthorizedUserRepository AUTHORIZED_USER_REPOSITORY;
    private final PasswordEncoder PASSWORD_ENCODER;

    @Autowired
    public AuthorizedUserDAO(AuthorizedUserRepository authorizedUserRepository, PasswordEncoder passwordEncoder) {
        this.AUTHORIZED_USER_REPOSITORY = authorizedUserRepository;
        this.PASSWORD_ENCODER = passwordEncoder;
    }

    /**
     * Retrieves validation code from the database.
     * @param id
     * @return
     */
    public String getValidationCodeById(UUID id) {
        return AUTHORIZED_USER_REPOSITORY.getCodeById(id);
    }

    /**
     * Updates the validationCode in the database.
     * @param validationCode
     * @param id
     */
    public void updateValidationCodeById(String validationCode, UUID id) {
        AUTHORIZED_USER_REPOSITORY.setCodeById(PASSWORD_ENCODER.encode(validationCode), id);
    }

    /**
     * Stores the user in the database.
     * @param authorizedUser
     * @return
     */
    public AuthorizedUser storeUser(AuthorizedUser authorizedUser) {
        return AUTHORIZED_USER_REPOSITORY.save(authorizedUser);
    }

}
