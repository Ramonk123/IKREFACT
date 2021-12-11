package nl.hsleiden.AfkoAPI.repositories;

import nl.hsleiden.AfkoAPI.models.Department;
import org.hibernate.type.UUIDBinaryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface DepartmentRepository extends JpaRepository<Department, UUIDBinaryType> {
    @Query(value = "SELECT * FROM department WHERE department_name = :department_name ORDER BY department_name ASC LIMIT 1 ;", nativeQuery = true)
    Department findByName(@Param("department_name") String department_name);
}
