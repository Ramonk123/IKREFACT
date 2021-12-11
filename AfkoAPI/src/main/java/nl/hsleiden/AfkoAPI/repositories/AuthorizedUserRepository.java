package nl.hsleiden.AfkoAPI.repositories;


import nl.hsleiden.AfkoAPI.models.AuthorizedUser;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;


@Transactional
public interface AuthorizedUserRepository extends UserRepository<AuthorizedUser> {
    Optional<AuthorizedUser> findAuthorizedUserById(UUID id);

    @Query(value = "SELECT validation_code FROM authorized_user WHERE id = :id ;", nativeQuery = true)
    String getCodeById(@Param("id") UUID id);

    @Modifying
    @Query(value = "UPDATE authorized_user SET validation_code = :newCode WHERE id = :id ;", nativeQuery = true)
    void setCodeById(@Param("newCode") String code, @Param("id") UUID id);

    @Query(value = "SELECT code FROM user WHERE LOWER(email) = LOWER(:email) ;", nativeQuery = true)
    String getCodeByEmail(@Param("email") String email);
}
