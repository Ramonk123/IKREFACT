package nl.hsleiden.AfkoAPI.controllers;

import nl.hsleiden.AfkoAPI.dao.AdminDAO;
import nl.hsleiden.AfkoAPI.dao.RoleDAO;
import nl.hsleiden.AfkoAPI.exceptions.*;
import nl.hsleiden.AfkoAPI.httpResponses.Response;
import nl.hsleiden.AfkoAPI.models.Admin;
import nl.hsleiden.AfkoAPI.models.AuthenticationResponse;
import nl.hsleiden.AfkoAPI.services.EmailSenderService;
import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Creates and deletes admin accounts from database
 * @author Daniel Paans
 */
@RestController
@RequestMapping("${DEFAULT_PATH}${ADMIN}")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminController {

    private final AdminDAO ADMIN_DAO;
    private final RoleDAO ROLE_DAO;
    private final EmailSenderService SENDER_SERVICE;

    @Autowired
    public AdminController(AdminDAO adminDAO, RoleDAO roleDAO, EmailSenderService senderService) {
        this.ADMIN_DAO = adminDAO;
        this.ROLE_DAO = roleDAO;
        this.SENDER_SERVICE = senderService;
    }

    /**
     * Creates an admin with role Admin if role is null and validates input.
     * @param admin
     * @return
     * @throws AccountCreationException
     */
    @Secured("ROLE_SUPER_ADMIN")
    @PostMapping
    public Admin createAdmin(@RequestBody Admin admin) throws AccountCreationException {
        if(admin.getRole() == null) { admin.setRole(ROLE_DAO.getRole("ROLE_ADMIN")); }

        try {
            Admin storedAdmin = ADMIN_DAO.storeAdmin(admin);
            UUID userId = storedAdmin.getId();
            String[] validationCodes = getCodesFromUserId(userId);
            encryptValidationCode(validationCodes[1], userId);
            sendMail(admin.getEmail(), validationCodes[0], validationCodes[1]);

            return storedAdmin;
        } catch(IllegalArgumentException iae) {
            throw new PasswordMissingException();
        } catch(DataIntegrityViolationException re) {
            if(re.getCause() instanceof PropertyValueException) {
                if(Objects.requireNonNull(re.getMessage()).contains("Admin.username")) {
                    throw new UsernameMissingException();
                } else if(re.getMessage().contains("Admin.email")) {
                    throw new EmailMissingException();
                } else {
                    re.printStackTrace();
                    throw new CreateAccountException();
                }
            } else if(re.getCause() instanceof ConstraintViolationException) {
                throw new AdminAlreadyExistsException();
            } else {
                re.printStackTrace();
                throw new CreateAccountException();
            }
        }
    }

    private void sendMail(String email, String username, String password){
        SENDER_SERVICE.sendEmail(email, "Dit zijn je tijdelijke inloggegevens", String.format("Gebruikersnaam: %s\nWachtwoord: %s", username, password));
    }

    private String[] getCodesFromUserId(UUID id) {
        return ADMIN_DAO.getValidationCodesById(id);
    }

    private void encryptValidationCode(String code, UUID id) {
        ADMIN_DAO.updateValidationCodeById(code, id);
    }

//    /**
//     * Deletes an admin when he receives an id
//     * @param id
//     * @return
//     */
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Response> removeAdmin(@PathVariable UUID id) {
//        ADMIN_DAO.deleteAdmin(id);
//        return ResponseEntity.ok(new Response("removed admin"));
//    }
    @Secured("ROLE_SUPER_ADMIN")
    @DeleteMapping("/{email}")
    public ResponseEntity<Response> removeAdmin(@PathVariable String email) {
        if(ADMIN_DAO.deleteAdmin(email)) {
            return ResponseEntity.ok(new Response("removed admin"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Response(HttpStatus.NOT_ACCEPTABLE.value(), "Admin with this email does not exist"));
        }
    }

    @Secured({"ROLE_ADMIN", "ROLE_SUPER_ADMIN"})
    @PutMapping("/{id}")
    public ResponseEntity<Response> changeCredentials(@PathVariable UUID id, @RequestParam("username") Optional<String> username,
                                                      @RequestParam("password") Optional<String> password) throws AccountCreationException {
        if(username.isPresent()) {
            if(username.get().startsWith("X") && username.get().endsWith("X") && username.get().length() == 8) {
                throw new AdminAlreadyExistsException();
            }
        }

        username.ifPresent(s -> ADMIN_DAO.changeUsername(id, s));
        password.ifPresent(s -> ADMIN_DAO.changePassword(id, s));
        return ResponseEntity.ok(new Response("Updated credentials"));
    }

    public Admin getAdminByUsername(String username) {
        return ADMIN_DAO.getAdminByUsername(username);
    }
}
