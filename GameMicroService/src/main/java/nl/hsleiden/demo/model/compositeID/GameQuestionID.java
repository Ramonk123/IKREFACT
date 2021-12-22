package nl.hsleiden.demo.model.compositeID;
import java.io.Serializable;


public class GameQuestionID implements Serializable {

    private String abbreviation_name;
    private String definition;

    public GameQuestionID() {}

    public GameQuestionID(String abbreviation_name, String definition) {
        this.abbreviation_name = abbreviation_name;
        this.definition = definition;
    }
}
