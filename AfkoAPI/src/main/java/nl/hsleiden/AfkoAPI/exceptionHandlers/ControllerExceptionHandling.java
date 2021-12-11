package nl.hsleiden.AfkoAPI.exceptionHandlers;

import nl.hsleiden.AfkoAPI.exceptions.*;
import nl.hsleiden.AfkoAPI.httpResponses.Response;
import nl.hsleiden.AfkoAPI.services.CSVParser;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * Handles all the exceptions that occur within the controllers.
 * @author Daniel Paans, Max Mulder
 */
@ControllerAdvice
public class ControllerExceptionHandling extends ResponseEntityExceptionHandler {

    /**
     * Handles all the SQLExceptions.
     * @author Daniel Paans
     * @param sqle
     * @return
     */
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Object> handleWhitelistException(SQLException sqle) {

        if(sqle.getSQLState().equals("45000")){
            return returnUnauthorized("Given email domain is not on the whitelist or email is not present");
        } else if(sqle.getErrorCode() == 1062 && sqle.getMessage().contains("admin")) {
            return returnBadRequest("Username already in use");
        } else {
            return new ResponseEntity<>("SQLException " + sqle.getSQLState() + ": " + sqle.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * @author Max Mulder
     */
    @ExceptionHandler(DepartmentDoesntExistException.class)
    public ResponseEntity<Object> handleDepartmentDoesntExistException(DepartmentDoesntExistException e) {
        Response response = new Response(HttpStatus.BAD_REQUEST.value(), "Can't find department");
        response.put("department", e.getDEPARTMENTNAME());
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * When there are no parameters used where they are expected.
     * @author Daniel Paans
     * @return
     */
    @ExceptionHandler(NoParamsUsedException.class)
    public ResponseEntity<Object> handleNoParamsUsedException() {
        return returnBadRequest("URL does not contain the required parameters");
    }

    /**
     * @author Daniel Paans
     * @return
     */
    @ExceptionHandler({IncorrectBodyException.class})
    public ResponseEntity<Object> handleIncorrectRequestBodyException() {
        return returnBadRequest("Can not process given object");
    }

    /**
     * @author Max Mulder
     * @return
     */
    @ExceptionHandler(CantParseException.class)
    public ResponseEntity<Object> handleCantParsesException() {
        return returnBadRequest("Can't parse CSV!");
    }

    /**
     * @author Max Mulder
     * @param e
     * @return
     */
    @ExceptionHandler(ColumnsNotFoundException.class)
    public ResponseEntity<Object> handleColumnsNotFoundException(ColumnsNotFoundException e) {
        Response response = new Response(HttpStatus.BAD_REQUEST.value(), "Can't find columns");
        response.put("missing_columns", e.getMissingColumns());
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * @author Max Mulder
     * @return
     */
    @ExceptionHandler(CSVNotFoundException.class)
    public ResponseEntity<Object> handleCSVNotFoundException() {
        return returnBadRequest("CSV not found!");
    }

    /**
     * @authro Max Mulder
     * @param e
     * @return
     */
    @ExceptionHandler(DepartmentColumnsNotFoundException.class)
    public ResponseEntity<Object> handleDepartmentColumnsNotFoundException(DepartmentColumnsNotFoundException e) {
        Response response = new Response(
                HttpStatus.BAD_REQUEST.value(),
                String.format("Could not split departments using: '%s' as dep_separator", e.getSeparator())
        );
        response.put(
                "ghost_department",
                e.getDepartmentName()
        );
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * @author Max Mulder
     * @param e
     * @return
     */
    @ExceptionHandler(DepartmentNotFoundException.class)
    public ResponseEntity<Object> handleDepartmentNotFoundException(DepartmentNotFoundException e) {
        return returnBadRequest(String.format("Department: '%s' does not exist!", e.getDepartment_name()));
    }

    /**
     * @author Max Mulder
     * @param e
     * @return
     */
    @ExceptionHandler(SeparatorNotSupportedException.class)
    public ResponseEntity<Object> handleSeparatorNotSupportedException(SeparatorNotSupportedException e) {
        Response response = new Response(
                HttpStatus.BAD_REQUEST.value(),
                String.format(
                        "Separator: '%s' is not supported! Take a look at the not supported separators",
                        e.getSeparator()
                ));
        response.put("not_supported_separators", CSVParser.getForbiddenSeparatorsUrl());
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * When the given report is not found in the database.
     * @author Daniel Paans
     * @param e
     * @return
     */
    @ExceptionHandler(ReportNotFoundException.class)
    public ResponseEntity<Object> handleReportNotFoundException(ReportNotFoundException e) {
        return returnBadRequest("Report not found");
    }

    // Security exceptions

    /**
     * When the wrong credentials are given during authentication.
     * @author Daniel Paans
     * @param e
     * @return
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException e) {
        return returnBadRequest(e.getMessage());
    }

    /**
     * When the user has no access to the request.
     * @author Daniel Paans
     * @param e
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException e) {
        return returnUnauthorized(e.getMessage());
    }

    /**
     * When the username already exists in the database.
     * @author Daniel Paans
     * @return
     */
    @ExceptionHandler(AdminAlreadyExistsException.class)
    public ResponseEntity<Object> handleAdminAlreadyExistsException() {
        return returnBadRequest("Username already exists or is not allowed");
    }

    /**
     * When the wrong role of a user is given in the requestbody.
     * @author Daniel Paans
     * @return
     */
    @ExceptionHandler(IncorrectRoleException.class)
    public ResponseEntity<Object> handleIncorrectRoleException() {
        return returnBadRequest("Given role does not exist");
    }

    /**
     * When there is no password given in the requestbody.
     * @author Daniel Paans
     * @return
     */
    @ExceptionHandler(PasswordMissingException.class)
    public ResponseEntity<Object> handlePasswordMissingException() {
        return returnBadRequest("No password given");
    }

    /**
     * When there is no username given in the requestbody.
     * @author Daniel Paans
     * @return
     */
    @ExceptionHandler(UsernameMissingException.class)
    public ResponseEntity<Object> handleUsernameMissingException() {
        return returnBadRequest("No username given");
    }

    /**
     * When there is no email given in the requestbody.
     * @author Daniel Paans
     * @return
     */
    @ExceptionHandler(EmailMissingException.class)
    public ResponseEntity<Object> handleEmailMissingException() {
        return returnBadRequest("No email given");
    }

    /**
     * To handle json errors within the requestbody.
     * @author Daniel Paans
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return returnBadRequest("Body is empty or incorrect");
    }

    private ResponseEntity<Object> returnBadRequest(String error) {
        return ResponseEntity.badRequest().body(new Response(HttpStatus.BAD_REQUEST.value(), error));
    }

    private ResponseEntity<Object> returnUnauthorized(String error) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response(HttpStatus.UNAUTHORIZED.value(), error));

    }
}
