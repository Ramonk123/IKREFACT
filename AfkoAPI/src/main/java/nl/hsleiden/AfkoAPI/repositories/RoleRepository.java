package nl.hsleiden.AfkoAPI.repositories;

import nl.hsleiden.AfkoAPI.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    @Query(value = "SELECT * FROM role WHERE name = :name ;", nativeQuery = true)
    Role findRoleByName(@Param("name") String name);
}
