package nl.hsleiden.AfkoAPI.dao;

import lombok.SneakyThrows;
import nl.hsleiden.AfkoAPI.models.Admin;
import nl.hsleiden.AfkoAPI.models.AdminDetails;
import nl.hsleiden.AfkoAPI.models.AuthorizedUser;
import nl.hsleiden.AfkoAPI.models.AuthorizedUserDetails;
import nl.hsleiden.AfkoAPI.repositories.AdminRepository;
import nl.hsleiden.AfkoAPI.repositories.AuthorizedUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * For authentication of a user.
 * @author Daniel Paans
 */
@Service
public class UserDetailsDAO implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private final AdminRepository ADMIN_REPOSITORY;
    private final AuthorizedUserRepository AUTHORIZED_USER_REPOSITORY;

    public UserDetailsDAO(AdminRepository adminRepository, AuthorizedUserRepository authorizedUserRepository) {
        this.ADMIN_REPOSITORY = adminRepository;
        this.AUTHORIZED_USER_REPOSITORY = authorizedUserRepository;
    }

    /**
     * Searches the database by username for a valid user and returns the UserDetails of that user.
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @SneakyThrows
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if(isValidUUID(username)){
            Optional<AuthorizedUser> authorizedUser = AUTHORIZED_USER_REPOSITORY.findAuthorizedUserById(UUID.fromString(username));
            authorizedUser.orElseThrow(() -> new UsernameNotFoundException("Not found: " + username));
            return authorizedUser.map(AuthorizedUserDetails::new).get();
        } else {
            try {
                Optional<Admin> admin = ADMIN_REPOSITORY.findAdminByUsername(username);
                admin.orElseThrow(() -> new UsernameNotFoundException("Not found: " + username));
                return admin.map(AdminDetails::new).get();
            } catch(IllegalArgumentException iae) {
                throw new UsernameNotFoundException("Not the right authority");
            }
        }
    }

    private boolean isValidUUID(String uuid) {
        final Pattern UUID_REGEX_PATTERN =
                Pattern.compile("^[{]?[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}[}]?$");

        if(uuid == null) {return false;}
        return UUID_REGEX_PATTERN.matcher(uuid).matches();
    }

}
