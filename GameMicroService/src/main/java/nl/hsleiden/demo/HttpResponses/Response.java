package nl.hsleiden.demo.HttpResponses;

import java.util.Date;
import java.util.HashMap;

/**
 * A HashMap wrapper that Can be used to make it easier to create http responses
 * @author Max Mulder
 */
public class Response extends HashMap<String, Object> {

    public Response() {
        this.put("timestamp", new Date());
    }

    /**
     * Create a Response with a timestamp and statuscode
     * @param statuscode HTTP statuscode
     */
    public Response(int statuscode) {
        this();
        this.put("status", statuscode);
    }

    /**
     * Create a Response with a timestamp, statuscode and error message
     * @param statuscode HTTP statuscode
     * @param error Errormessage
     */
    public Response(int statuscode, String error) {
        this(statuscode);
        this.put("error", error);
    }

    /**
     * Create a Response with a timestamp, statuscode and body
     * @param statuscode HTTP statuscode
     * @param body An object
     */
    public Response(int statuscode, Object body) {
        this(statuscode);
        this.put("body", body);
    }

    /**
     * Create a Response with a timestamp, statuscode(200) and body
     * @param body An Object
     */
    public Response(Object body) {
        this();
        this.put("status", 200);
        this.put("body", body);
    }
}
