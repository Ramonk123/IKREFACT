package nl.hsleiden.AfkoApp.DAO;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import nl.hsleiden.AfkoApp.Controllers.DotEnvController;
import nl.hsleiden.AfkoApp.Models.AbbreviationReport;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * For sending all the authentication requests.
 * @author Daniel Paans
 */
public class AuthenticationDAO {

    private static AuthenticationDAO authenticationDAO;
    DotEnvController dotEnvController;

    public synchronized static AuthenticationDAO getInstance() {
        if (authenticationDAO == null) {
            authenticationDAO = new AuthenticationDAO();
        }
        return authenticationDAO;
    }

    AuthenticationDAO () {
        dotEnvController = DotEnvController.getInstance();
    }

    //TODO: We need to implement the authorizedUser login first
//    public void CreateAuthorizedUser() {
//        try {
//            HttpResponse<JsonNode> jsonResponse =
//                    Unirest.post("http://localhost:8443/api/v1/user")
//                            .header("Content-Type", "text/plain")
//                            .body(body)
//                            .asJson();
//        } catch (UnirestException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * Sends an authentication request.
     * @param username
     * @param password
     * @return
     */
    public String AuthenticateUser(String username, String password) {
        JSONObject body = createrequestBody(username, password);
        try {
            HttpResponse<JsonNode> jsonResponse =
                Unirest.post(dotEnvController.getFromEnv("HOSTNAME") +
                                dotEnvController.getFromEnv("DEFAULT") + "/authenticate")
                        .header("Content-Type", "application/json")
                        .body(body)
                        .asJson();
            return jsonResponse.getBody().getObject().getString("jwt");

        } catch (UnirestException e) {
            e.printStackTrace();
            return null;
        }
    }

    private JSONObject createrequestBody(String username, String password) {
        HashMap<String, String> body = new HashMap();
        body.put("username", username);
        body.put("password", password);

        return new JSONObject(body);
    }
}
