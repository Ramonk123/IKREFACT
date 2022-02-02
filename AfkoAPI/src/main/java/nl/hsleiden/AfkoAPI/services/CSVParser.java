package nl.hsleiden.AfkoAPI.services;

import nl.hsleiden.AfkoAPI.controllers.DepartmentController;
import nl.hsleiden.AfkoAPI.dao.DepartmentDAO;
import nl.hsleiden.AfkoAPI.exceptions.*;
import nl.hsleiden.AfkoAPI.models.Abbreviation;
import nl.hsleiden.AfkoAPI.models.Department;
import org.springframework.beans.factory.support.ManagedSet;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

/**
 * Class that can be used to parse CSV's
 * @author Max Mulder
 */
@Service
public class CSVParser {
    private final static Set<String> FORBIDDEN_SEPARATORS_URL = FORBIDDEN_SET();
    private final DepartmentController DEPARTMENTCONTROLLER;

    CSVParser (DepartmentController departmentController) {
        this.DEPARTMENTCONTROLLER = departmentController;
    }

    private static Set<String> FORBIDDEN_SET() {
        Set<String> tmpForbiddenSeparatorsURL = new HashSet<>();
        tmpForbiddenSeparatorsURL.add(".");
        tmpForbiddenSeparatorsURL.add("[");
        tmpForbiddenSeparatorsURL.add("]");
        tmpForbiddenSeparatorsURL.add("{");
        tmpForbiddenSeparatorsURL.add("}");
        tmpForbiddenSeparatorsURL.add("|");
        tmpForbiddenSeparatorsURL.add("/");
        tmpForbiddenSeparatorsURL.add("\\");
        tmpForbiddenSeparatorsURL.add("\"");
        tmpForbiddenSeparatorsURL.add("%");
        tmpForbiddenSeparatorsURL.add("~");
        tmpForbiddenSeparatorsURL.add("#");
        tmpForbiddenSeparatorsURL.add("<");
        tmpForbiddenSeparatorsURL.add(">");
        tmpForbiddenSeparatorsURL.add("&");
        tmpForbiddenSeparatorsURL.add("?");
        tmpForbiddenSeparatorsURL.add("");
        tmpForbiddenSeparatorsURL.add(" ");
        tmpForbiddenSeparatorsURL.add("\n");
        tmpForbiddenSeparatorsURL.add("\\n");
        tmpForbiddenSeparatorsURL.add("^");
        tmpForbiddenSeparatorsURL.add("`");
        tmpForbiddenSeparatorsURL.add("$");
        tmpForbiddenSeparatorsURL.add("*");
        tmpForbiddenSeparatorsURL.add("+");
        return tmpForbiddenSeparatorsURL;
    }

    public static Set<String> getForbiddenSeparatorsUrl() {
        return FORBIDDEN_SEPARATORS_URL;
    }

    public static boolean isInForbiddenSeparatorsURL(String separator) {
        return FORBIDDEN_SEPARATORS_URL.contains(separator);
    }

    /**
     * Converts a CSV into a list of hashmaps
     * @param CSV CSV in string form
     * @param separator Char on with the string columns will be split
     * @return List of Hashmaps
     * @throws CantParseException Thrown when the CSV can't be parsed
     */
    private List<HashMap<String, Object>> CSVtoMap(String CSV, String separator) throws CantParseException{
        // setup vars
        List<HashMap<String, Object>> results = new ArrayList<>();

        try{
            // split data
            String[] data = CSV.split("\n");
            String[] columns = data[0].split(separator);

            // check if empty
            if (data.length <= 1) {throw new CantParseException();}

            // put data in map
            for (int row = 1; row < data.length; row++) {
                String[] entry = data[row].split(separator);
                HashMap<String, Object> tmpMap = new HashMap<>();
                for (int column = 0; column < columns.length; column++) {
                    if (entry.length > column) {
                        tmpMap.put(columns[column], entry[column]);
                    } else {
                        tmpMap.put(columns[column], "");
                    }
                }
                results.add(tmpMap);
            }
            return results;

        } catch (Exception e) {
            throw new CantParseException();
        }
    }

    /**
     * Converts a CSV into a list of Abbreviations
     * @param CSV CSV in string form
     * @param separator Char on which the string columns will be split
     * @param depSeperator Char on which the string departments will be split
     * @return List of Abbreviations
     */
    public List<Abbreviation> parseCSV(String CSV, String separator, String depSeperator) throws CantParseException, DepartmentColumnsNotFoundException, DepartmentDoesntExistException, ColumnsNotFoundException {
        List<HashMap<String, Object>> parsedCsv = CSVtoMap(CSV, separator);
        checkForMissingColumns(parsedCsv);
        return createAbbreviationListFromHashMap(parsedCsv, depSeperator);
    }

    private void checkForMissingColumns(List<HashMap<String, Object>> parsedCsv) throws ColumnsNotFoundException {
        List<String> missingColumns = new ArrayList<>();
        HashMap<String, Object> columnName = parsedCsv.get(0);
        if (columnName.containsKey("abbreviation_name")) {
            missingColumns.add("abbreviation_name");
        }
        if (!columnName.containsKey("definition")) {
            missingColumns.add("definition");
        }
        if (!columnName.containsKey("departments")) {
            missingColumns.add("departments");
        }
        if (missingColumns.size() > 0) {
            throw new ColumnsNotFoundException(missingColumns);
        }
    }

    private List<Abbreviation> createAbbreviationListFromHashMap(List<HashMap<String, Object>> parsedCsv, String depSeparator) throws DepartmentColumnsNotFoundException, DepartmentDoesntExistException {
        List<Abbreviation> abbreviations = new ArrayList<>();
        for (HashMap<String, Object> entry : parsedCsv) {
            Set<Department> departments = new ManagedSet<>();
            String[] departmentStringList = entry.get("departments").toString().split(depSeparator);
            System.out.println(departmentStringList.length);

            for (String departmentString : departmentStringList) {
                if (departmentString.length() < 1) {
                    throw new DepartmentColumnsNotFoundException(entry.get("departments").toString(), depSeparator);
                }

                Department department = DEPARTMENTCONTROLLER.getDepartmentByDepartmentName(departmentString);
                departments.add(department);

            }

            abbreviations.add(new Abbreviation(
                    departments,
                    (String) entry.get("abbreviation_name"),
                    (String) entry.get("definition"),
                    new Timestamp(Calendar.getInstance().getTime().getTime())
            ));
        }
        return abbreviations;
    }
//    This method has been refactored for the clean code assignment

//    public List<Abbreviation> Parse(String CSV, String separator, String depseparator) throws CantParseException, ColumnsNotFoundException, DepartmentColumnsNotFoundException, DepartmentDoesntExistException {
//        // parse csv
//        List<HashMap<String, Object>> parsedCsv = CSVtoMap(CSV, separator);
//
//        // check for columns
//        List<String> missingColumns = new ArrayList<>();
//        if (!parsedCsv.get(0).containsKey("abbreviation_name")) {
//            missingColumns.add("abbreviation_name");
//        }
//        if (!parsedCsv.get(0).containsKey("definition")) {
//            missingColumns.add("definition");
//        }
//        if (!parsedCsv.get(0).containsKey("departments")) {
//            missingColumns.add("departments");
//        }
//        if (missingColumns.size() > 0) {
//            throw new ColumnsNotFoundException(missingColumns);
//        }
//
//        // get abbreviations
//        List<Abbreviation> abbreviations = new ArrayList<>();
//
//        for (HashMap<String, Object> entry : parsedCsv) {
//            // get departments
//            Set<Department> departments = new ManagedSet<>();
//            String[] departmentStringList = entry.get("departments").toString().split(depseparator);
//            System.out.println(departmentStringList.length);
//
//            for (String departmentString : departmentStringList) {
//                //check if departmentList
//                if (departmentString.length() < 1) {
//                    throw new DepartmentColumnsNotFoundException(entry.get("departments").toString(), depseparator);
//                }
//
//                Department department = DEPARTMENTCONTROLLER.getDepartmentByDepartmentName(departmentString);
//                departments.add(department);
//
//            }
//
//            abbreviations.add(new Abbreviation(
//                    departments,
//                    (String) entry.get("abbreviation_name"),
//                    (String) entry.get("definition"),
//                    new Timestamp(Calendar.getInstance().getTime().getTime())
//            ));
//        }
//        return abbreviations;
//    }
}
