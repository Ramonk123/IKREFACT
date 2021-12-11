package nl.hsleiden.AfkoAPI.repositories;


import nl.hsleiden.AfkoAPI.models.AbbreviationReport;
import nl.hsleiden.AfkoAPI.models.User;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.UUID;


@NoRepositoryBean
public interface UserRepository<T extends User> extends JpaRepository<T, UUID> {
}
