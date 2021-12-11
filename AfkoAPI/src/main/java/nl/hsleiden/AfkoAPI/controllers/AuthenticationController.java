package nl.hsleiden.AfkoAPI.controllers;

import nl.hsleiden.AfkoAPI.JwtUtil;
import nl.hsleiden.AfkoAPI.dao.UserDetailsDAO;
import nl.hsleiden.AfkoAPI.httpResponses.Response;
import nl.hsleiden.AfkoAPI.models.AuthenticationResponse;
import nl.hsleiden.AfkoAPI.models.AuthenticationRequest;
import nl.hsleiden.AfkoAPI.models.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * Authenticates user and generate token
 * @author Daniel Paans
 */
@RestController
@RequestMapping("${DEFAULT_PATH}${AUTHENTICATE}")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthenticationController {

    private final AuthenticationManager AUTHENTICATION_MANAGER;
    private final UserDetailsDAO USER_DETAILS_DAO;
    private final JwtUtil JWT_TOKEN_UTIL;
    private final AdminController ADMIN_CONTROLLER;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, UserDetailsDAO userDetailsDAO, JwtUtil jwtTokenUtil, AdminController adminController) {
        this.AUTHENTICATION_MANAGER = authenticationManager;
        this.USER_DETAILS_DAO = userDetailsDAO;
        this.JWT_TOKEN_UTIL = jwtTokenUtil;
        ADMIN_CONTROLLER = adminController;
    }

    /**
     * Authenticates user and generate token.
     * @param authenticationRequest
     * @return
     * @throws AuthenticationException
     */
    @PostMapping
    public ResponseEntity<?> createAuthentication(@RequestBody AuthenticationRequest authenticationRequest) throws AuthenticationException {
        String username = authenticationRequest.getUsername();

        try {
            AUTHENTICATION_MANAGER.authenticate(
                    new UsernamePasswordAuthenticationToken(username, authenticationRequest.getPassword()));
        } catch (BadCredentialsException bce) {
            throw new BadCredentialsException("Bad credentials", bce);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final UserDetails USERDETAILS = USER_DETAILS_DAO.loadUserByUsername(username);
        final String JWT = JWT_TOKEN_UTIL.generateToken(USERDETAILS);

        Role role = ADMIN_CONTROLLER.getAdminByUsername(username).getRole();
        if(username.startsWith("X") && username.endsWith("X") && username.length() == 8) {
            return ResponseEntity.ok(new AuthenticationResponse(ADMIN_CONTROLLER.getAdminByUsername(username).getId(), role, JWT));
        } else {
            return ResponseEntity.ok(new AuthenticationResponse(null, role, JWT));
        }
    }

}
