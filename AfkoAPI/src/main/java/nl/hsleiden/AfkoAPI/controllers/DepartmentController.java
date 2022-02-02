package nl.hsleiden.AfkoAPI.controllers;


import nl.hsleiden.AfkoAPI.dao.DepartmentDAO;
import nl.hsleiden.AfkoAPI.exceptions.DepartmentDoesntExistException;
import nl.hsleiden.AfkoAPI.exceptions.DepartmentNotFoundException;
import nl.hsleiden.AfkoAPI.models.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Handles Department requests in the backend.
 * @author Rick Ketelaar, Max Mulder
 */
@RestController
@RequestMapping("${DEFAULT_PATH}${DEPARTMENT}")
@CrossOrigin(origins = "http://localhost:4200")
public class DepartmentController {

    private final DepartmentDAO DEPARTMENT_DAO;

    @Autowired
    public DepartmentController(DepartmentDAO departmentDAO){ this.DEPARTMENT_DAO = departmentDAO; }

    /**
     * Retrieves all the departments.
     * @author Rick Ketelaar
     * @return
     */
    @GetMapping
    public List<Department> getDepartments() {
        return DEPARTMENT_DAO.getDepartments();
    }

    /**
     * @author Max Mulder
     * @param departments
     * @return
     * @throws DepartmentNotFoundException
     */
    @PostMapping
    @Secured("ROLE_ADMIN")
    public List<Department> postDepartment(@RequestBody List<Department> departments) throws DepartmentNotFoundException {

        if (departments.isEmpty()) {throw new DepartmentNotFoundException();}
        return DEPARTMENT_DAO.postDepartment(departments);
    }

    /**
     * Retrieves all the departments by department name.
     * @author Rick Ketelaar
     * @param departmentName
     * @return
     * @throws DepartmentDoesntExistException
     */
    @GetMapping("/search")
    public Department getDepartmentByDepartmentName(@RequestParam("departmentName") String departmentName) throws DepartmentDoesntExistException {
        Department department = DEPARTMENT_DAO.getDepartmentByName(departmentName);
        if(department == null) {
            throw new DepartmentDoesntExistException(departmentName);
        } else {
            return department;
        }
    }
}
