package nl.hsleiden.demo.repository;


import nl.hsleiden.demo.model.GameQuestion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface GameQuestionRepository extends JpaRepository<GameQuestion, String> {

    @Query(value = "SELECT abbreviation_name, definition FROM question_view ORDER BY RAND() LIMIT 10;", nativeQuery = true)
    List<GameQuestion> getRandomQuestions();

    @Query(value = "SELECT abbreviation_name, definition FROM question_view ORDER BY RAND() LIMIT :limit ;", nativeQuery = true)
    List<GameQuestion> getRandomQuestions(@Param("limit") int limit);

    @Query(value = "SELECT abbreviation_name, definition FROM question_view WHERE departments LIKE :id ORDER BY RAND() LIMIT 10;", nativeQuery = true)
    List<GameQuestion> getRandomQuestionsDEP(@Param("id") String id);

    @Query(value = "SELECT abbreviation_name, definition FROM question_view WHERE departments LIKE :id ORDER BY RAND() LIMIT :limit ;", nativeQuery = true)
    List<GameQuestion> getRandomQuestionsDEP(@Param("id") String id, @Param("limit") int limit);
}
