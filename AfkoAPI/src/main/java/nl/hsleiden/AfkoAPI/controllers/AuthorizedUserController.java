package nl.hsleiden.AfkoAPI.controllers;


import lombok.Value;
import nl.hsleiden.AfkoAPI.dao.AuthorizedUserDAO;
import nl.hsleiden.AfkoAPI.dao.RoleDAO;
import nl.hsleiden.AfkoAPI.exceptions.AccountCreationException;
import nl.hsleiden.AfkoAPI.httpResponses.Response;
import nl.hsleiden.AfkoAPI.models.AuthorizedUser;
import nl.hsleiden.AfkoAPI.services.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Creates an authorizedUser in the database.
 * @author Daniel Paans
 */
@RestController
@RequestMapping("${DEFAULT_PATH}${USER}")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthorizedUserController {

    private final AuthorizedUserDAO AUTHORIZED_USER_DAO;
    private final RoleDAO ROLE_DAO;
    private final EmailSenderService SENDER_SERVICE;

    @Autowired
    public AuthorizedUserController(AuthorizedUserDAO authorizedUserDAO, RoleDAO roleDAO, EmailSenderService senderService) {
        this.AUTHORIZED_USER_DAO = authorizedUserDAO;
        this.ROLE_DAO = roleDAO;
        this.SENDER_SERVICE = senderService;
    }

    /**
     * Creates an authorizedUser and sends an email with a unique code for validation.
     * @param authorizedUser
     * @return
     * @throws AccountCreationException
     */
    @PostMapping
    public ResponseEntity<Response> postUser(@RequestBody AuthorizedUser authorizedUser) throws AccountCreationException {
        if (authorizedUser.getRole() == null) {
            authorizedUser.setRole(ROLE_DAO.getRole("ROLE_AUTHORIZED_USER"));
        }

        UUID userId = AUTHORIZED_USER_DAO.storeUser(authorizedUser).getId();
        String validationCode = getCodeFromUserId(userId);
        encryptValidationCode(validationCode, userId);
        sendMail(authorizedUser.getEmail(), validationCode);

        return ResponseEntity.ok(new Response(userId));
    }

    private void sendMail(String email, String code){
        SENDER_SERVICE.sendEmail(email, "Dit is jouw unieke code", code);
    }

    private String getCodeFromUserId(UUID id) {
        return AUTHORIZED_USER_DAO.getValidationCodeById(id);
    }

    private void encryptValidationCode(String code, UUID id) {
        AUTHORIZED_USER_DAO.updateValidationCodeById(code, id);
    }


}
