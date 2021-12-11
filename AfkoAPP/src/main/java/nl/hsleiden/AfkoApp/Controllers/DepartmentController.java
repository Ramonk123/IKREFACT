package nl.hsleiden.AfkoApp.Controllers;

import nl.hsleiden.AfkoApp.DAO.DepartmentDAO;
import nl.hsleiden.AfkoApp.Models.Department;
import java.util.ArrayList;
import java.util.UUID;

public class DepartmentController {
    static DepartmentController departmentController;

    private DepartmentDAO departmentDAO;

    /**
     * Creates instance of DepartmentController if it does not exist. Returns instance.
     * @author InsectByte
     * @return DepartmentController
     */
    public static synchronized DepartmentController getInstance() {
        if (departmentController == null) {
            departmentController = new DepartmentController();
        }
        return departmentController;
    }

    /**
     * Constructor for DepartmentController
     * @author InsectByte
     */
    private DepartmentController() {
        departmentDAO = DepartmentDAO.getInstance();
    }

    /**
     * Returns all Departments from DepartmentDAO
     * @author InsectByte
     * @return ArrayList<Department>
     */
    public ArrayList<Department> getDepartments() {
        return departmentDAO.fetchAllDepartments();
    }

    public Department fetchDepartmentByDepartmentName(String departmentName) {
        return departmentDAO.getCachedDepartmentByName(departmentName);
    }
}
