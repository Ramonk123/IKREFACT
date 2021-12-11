package nl.hsleiden.AfkoAPI.repositories;

import nl.hsleiden.AfkoAPI.dao.GameDAO;
import nl.hsleiden.AfkoAPI.models.Abbreviation;
import nl.hsleiden.AfkoAPI.models.GameQuestion;
import nl.hsleiden.AfkoAPI.models.GameScore;
import org.hibernate.type.UUIDBinaryType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public interface GameRepository extends JpaRepository<GameScore, UUIDBinaryType> {

    @Query(value = "SELECT ID, username, MAX(score) AS score, depID FROM gamescore WHERE depID = :depid GROUP BY username ORDER BY score DESC LIMIT :limit ;", nativeQuery = true)
    List<GameScore> findAllLimitDistinctFiltered(@Param("limit") int limit, @Param("depid") UUID depid);

    @Query(value = "SELECT * FROM gamescore WHERE depID = :depid ORDER BY score DESC LIMIT :limit ;", nativeQuery = true)
    List<GameScore> findAllLimitFiltered(@Param("limit") int limit, @Param("depid") UUID depid);

    @Query(value = "SELECT * FROM gamescore WHERE depID = :depid ORDER BY score DESC;", nativeQuery = true)
    List<GameScore> findAllFiltered(@Param("depid") UUID depid);

    @Query(value = "SELECT * FROM gamescore ORDER BY score DESC LIMIT :limit ;", nativeQuery = true)
    List<GameScore> findAllLimit(@Param("limit") int limit);

    @Query(value = "SELECT ID, username, MAX(score) AS score, depID FROM gamescore GROUP BY username ORDER BY score DESC LIMIT :limit ;", nativeQuery = true)
    List<GameScore> findAllLimitDistinct(@Param("limit") int limit);

    @Query(value = "SELECT ID, username, MAX(score) AS score, depID FROM gamescore GROUP BY username ORDER BY score DESC;", nativeQuery = true)
    List<GameScore> findAllDistinct();

    @Query(value = "SELECT COUNT(*) FROM abbreviation WHERE :abbreviation = abbreviation_name AND :description = definition;", nativeQuery = true)
    int checkAnswer(@Param("abbreviation") String abbreviation, @Param("description") String description);

    Optional<GameScore> findGameScoreById(UUID id);
}
