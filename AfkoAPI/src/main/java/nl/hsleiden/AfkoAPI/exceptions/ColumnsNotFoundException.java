package nl.hsleiden.AfkoAPI.exceptions;

import java.util.List;

/**
 * @author Max Mulder
 */
public class ColumnsNotFoundException extends CSVException{

    private final List<String> COLUMNS;

    public ColumnsNotFoundException(List<String> columns) {
        this.COLUMNS = columns;
    }

    public List<String> getMissingColumns() {
        return COLUMNS;
    }
}
