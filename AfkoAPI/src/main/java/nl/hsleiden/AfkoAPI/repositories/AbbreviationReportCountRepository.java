package nl.hsleiden.AfkoAPI.repositories;

import nl.hsleiden.AfkoAPI.models.AbbreviationReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;

@Repository
public interface AbbreviationReportCountRepository extends JpaRepository<AbbreviationReport, Object> {

    @Query(value = "SELECT * FROM abbreviation_report_counter_view ;", nativeQuery = true)
    List<Map<String, Object>> getReports();
}
