package nl.hsleiden.AfkoAPI.repositories;

import nl.hsleiden.AfkoAPI.models.AbbreviationReport;
import org.hibernate.type.UUIDBinaryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public interface AbbreviationReportRepository extends JpaRepository<AbbreviationReport, UUID> {

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM abbreviation_report WHERE abbreviation_id = :id ;", nativeQuery = true)
    void deleteAllReportsByAbbreviationId(@Param("id") UUID id);

}
