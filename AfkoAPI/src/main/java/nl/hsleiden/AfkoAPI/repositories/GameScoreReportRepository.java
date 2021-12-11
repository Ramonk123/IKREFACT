package nl.hsleiden.AfkoAPI.repositories;

import nl.hsleiden.AfkoAPI.models.AbbreviationReport;
import nl.hsleiden.AfkoAPI.models.GameScoreReport;
import org.hibernate.type.UUIDBinaryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public interface GameScoreReportRepository extends JpaRepository<GameScoreReport, UUID> {

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM game_score_report WHERE game_score_id = :id ;", nativeQuery = true)
    void deleteAllReportsByGameScoreId(@Param("id") UUID id);
}
