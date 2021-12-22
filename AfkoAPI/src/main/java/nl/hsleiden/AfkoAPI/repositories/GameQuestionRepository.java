package nl.hsleiden.AfkoAPI.repositories;

import nl.hsleiden.AfkoAPI.models.Abbreviation;
import nl.hsleiden.AfkoAPI.models.GameQuestion;
import nl.hsleiden.AfkoAPI.models.GameScore;
import org.hibernate.type.UUIDBinaryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Repository
@Transactional
public interface GameQuestionRepository extends JpaRepository<GameQuestion, String> {

    @Query(value = "SELECT abbreviation_name, definition FROM question_view ORDER BY RAND() LIMIT 10;", nativeQuery = true)
    List<GameQuestion> getRandomQuestions();

    @Query(value = "SELECT abbreviation_name, definition FROM question_view ORDER BY RAND() LIMIT :limit ;", nativeQuery = true)
    List<GameQuestion> getRandomQuestions(@Param("limit") int limit);
}
