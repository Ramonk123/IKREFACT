package nl.hsleiden.AfkoAPI.repositories;

import nl.hsleiden.AfkoAPI.models.Admin;
import nl.hsleiden.AfkoAPI.models.GameScoreReport;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Transactional
public interface AdminRepository extends UserRepository<Admin> {
    Optional<Admin> findAdminByUsername(String username);

    @Modifying
    @Query(value = "DELETE FROM admin WHERE id = :id ;", nativeQuery = true)
    void deleteAdminById(@Param("id") UUID id);

    @Modifying
    @Query(value = "DELETE FROM user WHERE id = :id ;", nativeQuery = true)
    void deleteUserById(@Param("id") UUID id);

    @Query(value = "SELECT id FROM user WHERE email = :email ;", nativeQuery = true)
    byte[][] getAdminIdByEmail(@Param("email") String email);

    @Modifying
    @Query(value = "DELETE FROM user_roles WHERE user_id = :id ;", nativeQuery = true)
    void deleteUserRolesById(@Param("id") UUID id);

    @Modifying
    @Query(value = "UPDATE admin SET password = :password WHERE id = :id ;", nativeQuery = true)
    void updatePasswordById(@Param("id") UUID id, @Param("password") String password);

    @Modifying
    @Query(value = "UPDATE admin SET username = :username WHERE id = :id ;", nativeQuery = true)
    void updateUsernameById(@Param("id") UUID id, @Param("username") String username);

    @Query(value = "SELECT username, password FROM admin WHERE id = :id ;", nativeQuery = true)
    String getCodesById(@Param("id") UUID id);

    @Modifying
    @Query(value = "UPDATE admin SET password = :newCode WHERE id = :id ;", nativeQuery = true)
    void setCodeById(@Param("newCode") String code, @Param("id") UUID id);

    @Query(value = "SELECT * FROM admin WHERE username = :username ;", nativeQuery = true)
    Admin getAdminByUsername(String username);

}
