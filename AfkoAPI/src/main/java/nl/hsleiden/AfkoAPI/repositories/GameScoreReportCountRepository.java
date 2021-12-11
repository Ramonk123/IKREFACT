package nl.hsleiden.AfkoAPI.repositories;

import nl.hsleiden.AfkoAPI.models.GameScoreReport;
import org.hibernate.type.UUIDBinaryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GameScoreReportCountRepository extends JpaRepository<GameScoreReport, Object> {

    @Query(value = "SELECT * FROM game_score_report_counter_view ;", nativeQuery = true)
    List<Map<String, Object>> getReports();
}
