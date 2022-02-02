package nl.hsleiden.demo.repository;


import nl.hsleiden.demo.model.GameScore;
import org.hibernate.type.UUIDBinaryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public interface GameRepository extends JpaRepository<GameScore, UUIDBinaryType> {


    @Query(value = "SELECT * FROM gamescore WHERE depID = :depid ORDER BY score DESC LIMIT 10;", nativeQuery = true)
    List<GameScore> findAllFiltered(@Param("depid") UUID depid);


    @Query(value = "SELECT ID, username, MAX(score) AS score, depID FROM gamescore GROUP BY username ORDER BY score DESC LIMIT 10;", nativeQuery = true)
    List<GameScore> findAllDistinct();

    @Query(value = "SELECT COUNT(*) FROM abbreviation WHERE :abbreviation = abbreviation_name AND :description = definition LIMIT 10;", nativeQuery = true)
    int checkAnswer(@Param("abbreviation") String abbreviation, @Param("description") String description);

    Optional<GameScore> findGameScoreById(UUID id);
}
