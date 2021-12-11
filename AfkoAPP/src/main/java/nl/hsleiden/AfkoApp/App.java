package nl.hsleiden.AfkoApp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.hsleiden.AfkoApp.Controllers.DotEnvController;
import nl.hsleiden.AfkoApp.Controllers.HomeController;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    public static App app;

    private HomeController homeController;
    DotEnvController dotEnvController;

    /**
     * Creates instance of App if it does not exist, returns Instance.
     * @author InsectByte
     * @return App
     */
    public static synchronized App getInstance() {
        if (app == null) {
            app = new App();
        }
        return app;
    }

    /**
     * Starts this beauty of an application
     * @author InsectByte
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        homeController = HomeController.getInstance();
        dotEnvController = DotEnvController.getInstance();

        scene = new Scene(loadFXML("home"), 640, 480);
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Changes the root of the scene. Practically just changing the scene entirely.
     * @author InsectByte
     * @param fxml
     */
    public void switchScene(String fxml) {
        try{
            scene.setRoot(loadFXML(fxml));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads FXML Files on supplied string name
     * @author InsectByte
     * @param fxml
     * @return
     * @throws IOException
     */
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("fxml/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    /**
     * Function that starts it all
     * @author InsectByte
     * @param args
     */
    public static void main(String[] args) {
        launch();
    }

}