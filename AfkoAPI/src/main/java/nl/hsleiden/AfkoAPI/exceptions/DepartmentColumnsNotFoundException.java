package nl.hsleiden.AfkoAPI.exceptions;

/**
 * @author Max Mulder
 */
public class DepartmentColumnsNotFoundException extends CSVException{

    private String departmentName;
    private String separator;

    public DepartmentColumnsNotFoundException(String department_name, String separator) {
        this.departmentName = department_name;
        this.separator = separator;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public String getSeparator() {
        return separator;
    }
}
