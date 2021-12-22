package nl.hsleiden.demo.model;



import nl.hsleiden.demo.model.compositeID.GameQuestionID;
import javax.persistence.*;


@Entity
@Table(name = "question_view")
@IdClass(GameQuestionID.class)
public class GameQuestion {

    @Id
    private String abbreviation_name;
    @Id
    private String definition;

    public GameQuestion() {}

    public GameQuestion(String abbreviation_name, String definition) {
        this.abbreviation_name = abbreviation_name;
        this.definition = definition;
    }

    public String getAbbreviation_name() {
        return abbreviation_name;
    }

    public void setAbbreviation_name(String abbreviation_name) {
        this.abbreviation_name = abbreviation_name;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }
}
