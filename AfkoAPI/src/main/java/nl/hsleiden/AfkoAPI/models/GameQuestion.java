package nl.hsleiden.AfkoAPI.models;


import nl.hsleiden.AfkoAPI.models.compositeID.GameQuestionID;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.Subselect;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "question_view")
@IdClass(GameQuestionID.class)
public class GameQuestion{

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
