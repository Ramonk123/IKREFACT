package nl.hsleiden.AfkoApp.Controllers;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Class for handling everything dotEnv related.
 * @author InsectByte
 */
public class DotEnvController {
    static DotEnvController dotEnvController;

    Dotenv dotenv;

    /**
     * Creates instance of DotEnvController, if there is no Instance. Returns Instance of DotEnvController
     * @author InsectByte
     * @return DotEnvController
     */
    public static synchronized DotEnvController getInstance() {
        if (dotEnvController == null) {
            dotEnvController = new DotEnvController();
        }
        return dotEnvController;
    }

    /**
     * Constructor for DotEnvController
     * @author Wessel
     */
    private DotEnvController () {
        dotenv = Dotenv.load();
    }

    /**
     * Returns value of associated Key based on param key.
     * @author Wessel
     * @param key
     * @return
     */
    public String getFromEnv(String key) {
        return dotenv.get(key);
    }
}
