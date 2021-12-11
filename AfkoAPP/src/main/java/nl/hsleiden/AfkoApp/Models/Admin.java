package nl.hsleiden.AfkoApp.Models;


public class Admin {

    String JWTToken;

    public Admin() {}

    /**
     * Admin Constructor
     * @author InsectByte
     * @param jwt
     */
    public Admin(String jwt) {JWTToken = jwt;}

    /**
     * Returns JWT Token from Admin
     * @author InsectByte
     * @return
     */
    public String getJWTToken() {
        return JWTToken;
    }

    /**
     * Sets JWT Token
     * @author InsectByte
     * @param JWTToken
     */
    public void setJWTToken(String JWTToken) {
        this.JWTToken = JWTToken;
    }
}
