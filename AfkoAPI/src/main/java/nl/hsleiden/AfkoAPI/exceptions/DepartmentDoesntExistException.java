package nl.hsleiden.AfkoAPI.exceptions;

/**
 * @author Max Mulder
 */
public class DepartmentDoesntExistException extends Exception {
    private final String DEPARTMENTNAME;

    public DepartmentDoesntExistException(String departmentName) {
        this.DEPARTMENTNAME = departmentName;
    }

    public String getDEPARTMENTNAME() {
        return DEPARTMENTNAME;
    }
}
