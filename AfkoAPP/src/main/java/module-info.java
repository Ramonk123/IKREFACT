module nl.hsleiden {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.json;
    requires java.sql;
    requires unirest.java;
    requires com.jfoenix;

    opens nl.hsleiden.AfkoApp.Views;
    requires dotenv.java;
    exports nl.hsleiden.AfkoApp.Views;
    exports nl.hsleiden.AfkoApp;
}
