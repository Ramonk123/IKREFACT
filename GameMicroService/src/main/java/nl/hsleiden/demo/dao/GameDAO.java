package nl.hsleiden.demo.dao;


import nl.hsleiden.demo.model.GameQuestion;
import nl.hsleiden.demo.model.GameScore;
import nl.hsleiden.demo.repository.GameQuestionRepository;
import nl.hsleiden.demo.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Service
public class GameDAO {

    private final GameRepository GAME_REPOSITORY;
    private final GameQuestionRepository GAME_QUESTION_REPOSITORY;

    @Autowired
    public GameDAO(GameRepository gameRepository, GameQuestionRepository gameQuestionRepository) {
        this.GAME_REPOSITORY = gameRepository;
        this.GAME_QUESTION_REPOSITORY = gameQuestionRepository;
    }

    public List<GameScore> getScores() {return GAME_REPOSITORY.findAll();}

    public List<GameScore> getScoresFiltered(UUID depID) {
        return GAME_REPOSITORY.findAllFiltered(depID);
    }


    /**
     * @author Max Mulder
     * @return
     */
    public List<GameScore> getScoresDistinct(){return GAME_REPOSITORY.findAllDistinct();}

    public Optional<GameScore> getScoreFromId(UUID id) {
        return GAME_REPOSITORY.findGameScoreById(id);
    }

    /**
     * @author Max Mulder
     * @return
     */
    public List<GameQuestion> getRandomQuestions() {return GAME_QUESTION_REPOSITORY.getRandomQuestions();}

    /**
     * @author Max Mulder
     * @return
     */
    public List<GameQuestion> getRandomQuestions(int limit) {return GAME_QUESTION_REPOSITORY.getRandomQuestions(limit);}

    /**
     * @author Max Mulder
     * @return
     */
    public List<GameQuestion> getRandomQuestions(String id) {return GAME_QUESTION_REPOSITORY.getRandomQuestionsDEP(id);}

    /**
     * @author Max Mulder
     * @return
     */
    public List<GameQuestion> getRandomQuestions(String id, int limit) {return GAME_QUESTION_REPOSITORY.getRandomQuestionsDEP(id, limit);}

    /**
     * Post a gameScore to the database
     * @author Max Mulder
     * @param score
     * @return
     */
    public GameScore postScore(GameScore score) {
        return GAME_REPOSITORY.save(score);
    }

    /**
     * Removes score from the database.
     * @author Daniel Paans
     * @param gameScore
     * @return
     */
    public String removeScore(GameScore gameScore) {
        GAME_REPOSITORY.delete(gameScore);
        return "succes";
    }

    /**
     * @author Max Muler
     * @param gamequestion
     * @return
     */
    public boolean checkAnswer(GameQuestion gamequestion) {
        return 0 < GAME_REPOSITORY.checkAnswer(gamequestion.getAbbreviation_name(), gamequestion.getDefinition());
    }
}
