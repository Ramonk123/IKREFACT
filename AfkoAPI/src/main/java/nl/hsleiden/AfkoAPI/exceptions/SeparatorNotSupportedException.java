package nl.hsleiden.AfkoAPI.exceptions;

/**
 * @author Max Mulder
 */
public class SeparatorNotSupportedException extends CSVException{

    private String separator;

    public SeparatorNotSupportedException(String separator) {
        this.separator = separator;
    }

    public String getSeparator() {
        return separator;
    }
}
