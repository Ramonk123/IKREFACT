package nl.hsleiden.AfkoAPI.exceptions;

/**
 * @author Max Mulder
 */
public class DepartmentNotFoundException extends Exception{

    private String departmentName;

    public DepartmentNotFoundException() {}

    public DepartmentNotFoundException(String department_name) {
        this.departmentName = department_name;
    }

    public String getDepartment_name() {
        return departmentName;
    }
}
