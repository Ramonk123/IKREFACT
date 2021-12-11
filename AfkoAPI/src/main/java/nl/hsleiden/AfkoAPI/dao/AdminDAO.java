package nl.hsleiden.AfkoAPI.dao;

import nl.hsleiden.AfkoAPI.models.Admin;
import nl.hsleiden.AfkoAPI.repositories.AdminRepository;
import org.hibernate.type.UUIDBinaryType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.UUID;
import java.util.function.Function;

/**
 * To store and remove admins in the database.
 * @author Daniel Paans
 */
@Service
public class AdminDAO {

    private final AdminRepository ADMIN_REPOSITORY;
    private final PasswordEncoder PASSWORD_ENCODER;

    @Autowired
    public AdminDAO(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.ADMIN_REPOSITORY = adminRepository;
        this.PASSWORD_ENCODER = passwordEncoder;
    }

    public String changePassword(UUID id, String password) {
        ADMIN_REPOSITORY.updatePasswordById(id, PASSWORD_ENCODER.encode(password));
        return "succes";
    }

    public String changeUsername(UUID id, String username) {
        ADMIN_REPOSITORY.updateUsernameById(id, username);
        return "succes";
    }

    /**
     * Stores the admin in the database.
     * @param admin
     * @return
     */
    public Admin storeAdmin(Admin admin) {
        return ADMIN_REPOSITORY.save(admin);
    }
    public String[] getValidationCodesById(UUID id) {
        return ADMIN_REPOSITORY.getCodesById(id).split(",");
    }

    public void updateValidationCodeById(String validationCode, UUID id) {
        ADMIN_REPOSITORY.setCodeById(PASSWORD_ENCODER.encode(validationCode), id);
    }

    public Admin getAdminByUsername(String username) {
        return ADMIN_REPOSITORY.getAdminByUsername(username);
    }

    /**
     * Removes admin from the database.
     * @param id
     * @return
     */
    public String deleteAdmin(UUID id) {
        ADMIN_REPOSITORY.deleteAdminById(id);
        removeDependencies(id);
        return "succes";
    }

    public boolean deleteAdmin(String email) {
        byte[][] ids = ADMIN_REPOSITORY.getAdminIdByEmail(email);
        for(byte[] id : ids) {
            UUID uuid = byteArrayToUUID(id);
            ADMIN_REPOSITORY.deleteAdminById(uuid);
            removeDependencies(uuid);
        }
        return ids.length > 0;
    }

    private void removeDependencies(UUID id) {
//        ADMIN_REPOSITORY.deleteUserRolesById(id);
        ADMIN_REPOSITORY.deleteUserById(id);
    }

    private UUID byteArrayToUUID(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        return new UUID(bb.getLong(), bb.getLong());
    }
}
