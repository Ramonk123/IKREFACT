package nl.hsleiden.demo.controllers;

import nl.hsleiden.demo.HttpResponses.Response;
import nl.hsleiden.demo.dao.GameDAO;
import nl.hsleiden.demo.model.GameQuestion;
import nl.hsleiden.demo.model.GameScore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("${DEFAULT_PATH}${GAME}")
@CrossOrigin(origins = "http://localhost:4200")
public class GameController {
    private final GameDAO GAME_DAO;

    @Autowired
    public GameController(GameDAO gameDAO) {
        this.GAME_DAO = gameDAO;
    }

    /**
     * checks the awnser of a given question
     *
     * @param gamequestion
     * @return
     * @author Max Mulder
     */
    @GetMapping
    public boolean checkAnswer(@RequestBody GameQuestion gamequestion) {
        return GAME_DAO.checkAnswer(gamequestion);
    }

    @GetMapping("/scoreboard/{id}")
    public Optional<GameScore> getScore(@PathVariable UUID id) {
        return GAME_DAO.getScoreFromId(id);
    }

    /**
     * REFACTORING getScores()
     * Partly dead code. Some parameters and if statements are never called.
     * This is because the distinct value is always set to TRUE by the front end.
     */
//        @GetMapping("/scoreboard")
//        public List<GameScore> getScores(
//                @RequestParam(value = "limit", required = false) Optional<Integer> limit,
//                @RequestParam(value = "distinct", required = false) Optional<Boolean> distinct,
//                @RequestParam(value = "filter", required = false) Optional<UUID> depFilter
//        ) {
//            boolean isFiltered = depFilter.isPresent();
//            boolean isLimit = limit.isPresent();
//            boolean isDistinct = distinct.isPresent() && distinct.get();
//
//            if (isFiltered && isDistinct && isLimit) {return GAME_DAO.getScoresLimitDistinctFiltered(limit.get(), depFilter.get());}
//            if (isFiltered && isLimit) {return GAME_DAO.getScoresLimitFiltered(limit.get(), depFilter.get());}
//            if (isDistinct && isLimit) {return GAME_DAO.getScoresLimitDistinct(limit.get());}
//            if (isFiltered) {return GAME_DAO.getScoresFiltered(depFilter.get());}
//            if (isLimit) {return GAME_DAO.getScoresLimit(limit.get());}
//            if (isDistinct) {return GAME_DAO.getScoresDistinct();}
//            return GAME_DAO.getScores();
//        }
    @GetMapping("/scoreboard")
    public List<GameScore> getScores(@RequestParam(value = "filter", required = false) Optional<UUID> depFilter) {
        boolean isFiltered = depFilter.isPresent();

        if (isFiltered) {
            return GAME_DAO.getScoresFiltered(depFilter.get());
        }


        return GAME_DAO.getScores();
    }

    /**
     * Adds a new abbreviation to the database.
     *
     * @param score
     * @return
     * @author Rick Ketelaar
     */
    @PostMapping("/scoreboard")
    public GameScore postScore(@RequestBody GameScore score) {
        return GAME_DAO.postScore(score);
    }

    /**
     * Deletes a gamescore from the database.
     *
     * @param gameScore
     * @return
     * @author Daniel Paans
     */
    @DeleteMapping()
    public ResponseEntity<Response> deleteGameScore(@RequestBody GameScore gameScore) {
        GAME_DAO.removeScore(gameScore);
        return ResponseEntity.ok(new Response(String.format("Deleted game score %s (%s) with score: %s",
                gameScore.getUsername(), gameScore.getId(), gameScore.getScore())));
    }


    /**
     * Gets gameQuestions based on a limit, default limit is 10
     *
     * @param limit
     * @return
     * @author Max Mulder
     */
    @GetMapping("/questions")
    public List<GameQuestion> getGameQuestions(@RequestParam(value = "limit", required = false) Optional<Integer> limit) {
        if (limit.isPresent()) {
            return GAME_DAO.getRandomQuestions(limit.get());
        }
        return GAME_DAO.getRandomQuestions();
    }


//        /**
//         * Gets gameQuestions based on distinct and a limit, default limit is 10
//         * @author Max Mulder
//         * @param id
//         * @param limit
//         * @return
//         */
//        @GetMapping("/questions/{id}")
//        public List<GameQuestion> getGameQuestions(@PathVariable(value = "id", required = false) Optional<String> id, @RequestParam(value = "limit", required = false) Optional<Integer> limit) {
//
//            if (id.isPresent() && limit.isPresent()) {
//                return GAME_DAO.getRandomQuestions("%" + id.get() + "%", limit.get());
//            }
//            if (id.isPresent()) {return GAME_DAO.getRandomQuestions("%" + id.get() + "%");}
//            if (limit.isPresent()) {return GAME_DAO.getRandomQuestions(limit.get());}
//            return GAME_DAO.getRandomQuestions();
//        }

}

