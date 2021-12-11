package nl.hsleiden.AfkoApp.Controllers;

import nl.hsleiden.AfkoApp.DAO.AuthenticationDAO;

import java.util.UUID;

/**
 * To handle authentication requests made by the view.
 * @author Daniel Paans
 */
public class AuthenticationController {

    private AuthenticationDAO authenticationDAO;

    private static AuthenticationController authenticationController;

    public static AuthenticationController getInstance() {
        if (authenticationController == null) {
            authenticationController = new AuthenticationController();
        }
        return authenticationController;
    }

    private AuthenticationController() {
        this.authenticationDAO = AuthenticationDAO.getInstance();
    }

    /**
     * Authenticates an admin.
     * @param username
     * @param password
     * @return
     */
    public String authenticate(String username, String password) {
        return authenticationDAO.AuthenticateUser(username, password);
    }

    //TODO: First we need to implement the login sequence for an authorized user in the views
//    public void authenticate(UUID id, String validationCode) {
//        authenticationDAO.AuthenticateUser(id.toString(), validationCode);
//    }
//
//    public void createAuthorizedUser(String email) {
//
//    }
}
