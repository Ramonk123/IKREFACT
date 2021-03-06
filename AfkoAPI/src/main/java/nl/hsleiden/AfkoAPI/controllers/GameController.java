package nl.hsleiden.AfkoAPI.controllers;

import nl.hsleiden.AfkoAPI.dao.GameDAO;
import nl.hsleiden.AfkoAPI.httpResponses.Response;
import nl.hsleiden.AfkoAPI.models.AbbreviationReport;
import nl.hsleiden.AfkoAPI.models.GameScore;
import nl.hsleiden.AfkoAPI.models.GameQuestion;
import nl.hsleiden.AfkoAPI.models.GameScore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Max Mulder, Rick Ketelaar
 */
@RestController
@RequestMapping("${DEFAULT_PATH}${GAME}")
@CrossOrigin(origins = "http://localhost:4200")
public class GameController {
    private final GameDAO GAME_DAO;

    @Autowired
    public GameController(GameDAO gameDAO) {this.GAME_DAO = gameDAO;}

    /**
     * checks the awnser of a given question
     * @author Max Mulder
     * @param gamequestion
     * @return
     */
    @GetMapping
    public boolean checkAnswer(@RequestBody GameQuestion gamequestion) {
        return GAME_DAO.checkAnswer(gamequestion);
    }

    /**
     * gets gameScores
     * @author Max Mulder
     * @param limit
     * @param distinct
     * @return
     */
    @GetMapping("/scoreboard")
    public List<GameScore> getScores(
            @RequestParam(value = "limit", required = false) Optional<Integer> limit,
            @RequestParam(value = "distinct", required = false) Optional<Boolean> distinct,
            @RequestParam(value = "filter", required = false) Optional<UUID> depFilter
    ) {
        boolean isFiltered = depFilter.isPresent();
        boolean isLimit = limit.isPresent();
        boolean isDistinct = distinct.isPresent() && distinct.get();

        if (isFiltered && isDistinct && isLimit) {return GAME_DAO.getScoresLimitDistinctFiltered(limit.get(), depFilter.get());}
        if (isFiltered && isLimit) {return GAME_DAO.getScoresLimitFiltered(limit.get(), depFilter.get());}
        if (isDistinct && isLimit) {return GAME_DAO.getScoresLimitDistinct(limit.get());}
        if (isFiltered) {return GAME_DAO.getScoresFiltered(depFilter.get());}
        if (isLimit) {return GAME_DAO.getScoresLimit(limit.get());}
        if (isDistinct) {return GAME_DAO.getScoresDistinct();}
        return GAME_DAO.getScores();
    }

    /**
     * Adds a new abbreviation to the database.
     * @author Rick Ketelaar
     * @param score
     * @return
     */
    @PostMapping("/scoreboard")
    public GameScore postScore(@RequestBody GameScore score) {
        return GAME_DAO.postScore(score);
    }

    /**
     * Deletes a gamescore from the database.
     * @author Daniel Paans
     * @param gameScore
     * @return
     */
    @DeleteMapping()
    public ResponseEntity<Response> deleteGameScore(@RequestBody GameScore gameScore) {
        GAME_DAO.removeScore(gameScore);
        return ResponseEntity.ok(new Response(String.format("Deleted game score %s (%s) with score: %s",
                                                    gameScore.getUsername(), gameScore.getId(), gameScore.getScore())));
    }



    /**
     * Gets gameQuestions based on a limit, default limit is 10
     * @author Max Mulder
     * @param limit
     * @return
     */
    @GetMapping("/questions")
    public List<GameQuestion> getGameQuestions(@RequestParam(value = "limit", required = false) Optional<Integer> limit) {
        if (limit.isPresent()) {return GAME_DAO.getRandomQuestions(limit.get());}
        return GAME_DAO.getRandomQuestions();
    }
}
