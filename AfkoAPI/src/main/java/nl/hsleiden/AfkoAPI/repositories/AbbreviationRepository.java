package nl.hsleiden.AfkoAPI.repositories;

import nl.hsleiden.AfkoAPI.models.Abbreviation;
import org.hibernate.type.UUIDBinaryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public interface AbbreviationRepository extends JpaRepository<Abbreviation, UUIDBinaryType> {

    @Query(value = "SELECT * FROM abbreviation WHERE hidden = false ORDER BY added DESC LIMIT 10;", nativeQuery = true)
    List<Abbreviation> getRecent();

    @Query(value = "SELECT * FROM abbreviation WHERE abbreviation_name LIKE CONCAT(:keyword, '%') AND hidden = false;",nativeQuery = true)
    Optional<List<Abbreviation>> findAbbreviationsByAbbreviation_name(@Param("keyword") String keyword);

    @Query(value = "SELECT * FROM full_afko_view WHERE departments LIKE CONCAT('%', :depID, '%') AND abbreviation_name LIKE CONCAT(:keyword, '%');", nativeQuery = true)
    Optional<List<Abbreviation>> findAbbreviationsByAbbreviation_name(@Param("keyword") String keyword, @Param("depID") String id);

    @Query(value = "SELECT * FROM full_afko_view WHERE departments LIKE CONCAT('%', :depID, '%')", nativeQuery = true)
    List<Abbreviation> findAbbreviationsByDepartmentId(@Param("depID") String id);

    @Query(value = "SELECT * FROM abbreviation WHERE abbreviation_name >= :value1 AND abbreviation_name <= :value2 AND hidden = false",nativeQuery = true)
    Optional<List<Abbreviation>> findAbbreviationsByCategory(@Param("value1") String value1, @Param("value2") String value2);

    @Transactional
    @Modifying
    @Query(value = "UPDATE abbreviation SET abbreviation_name = :abbreviationName, definition = :definition WHERE ID = :id ;" , nativeQuery = true)
    void editAbbreviation(@Param("id") UUID id, @Param("abbreviationName") String abbreviationName, @Param("definition") String definition);

    Optional<Abbreviation> findAbbreviationById(UUID id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE abbreviation SET hidden = true WHERE ID = :id ;", nativeQuery = true)
    void hideAbbreviation(@Param("id") UUID id);
}
