package nl.hsleiden.AfkoAPI.dao;


import nl.hsleiden.AfkoAPI.exceptions.DepartmentNotFoundException;
import nl.hsleiden.AfkoAPI.models.Department;
import nl.hsleiden.AfkoAPI.repositories.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class DepartmentDAO {

    private final DepartmentRepository DEPARTMENT_REPOSITORY;

    @Autowired
    public DepartmentDAO(DepartmentRepository departmentRepository) {
        this.DEPARTMENT_REPOSITORY = departmentRepository;
    }

    public List<Department> getDepartments() {
        return DEPARTMENT_REPOSITORY.findAll();
    }

    public Department getDepartmentByName(String name) {return DEPARTMENT_REPOSITORY.findByName(name);}

    public List<Department> postDepartment(List<Department> departments) throws DepartmentNotFoundException {
        for (Department department: departments) {
            String department_name = department.getDepartment_name();
            if (department_name == null || department_name.isEmpty()) {
                throw new DepartmentNotFoundException(null);
            }

            department.setId(UUID.randomUUID());
        }

        return DEPARTMENT_REPOSITORY.saveAll(departments);
    }
}
