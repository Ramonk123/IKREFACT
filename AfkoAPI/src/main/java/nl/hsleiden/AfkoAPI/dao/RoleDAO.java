package nl.hsleiden.AfkoAPI.dao;

import nl.hsleiden.AfkoAPI.models.Role;
import nl.hsleiden.AfkoAPI.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * To retrieve all the roles from the database.
 * @author Daniel Paans
 */
@Service
public class RoleDAO {

    private final RoleRepository ROLE_REPOSITORY;

    @Autowired
    public RoleDAO(RoleRepository roleRepository) {
        this.ROLE_REPOSITORY = roleRepository;
    }

    public Role getRole(String name) {
        return ROLE_REPOSITORY.findRoleByName(name);
    }


}
