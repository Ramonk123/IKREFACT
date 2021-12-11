package nl.hsleiden.AfkoAPI;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import lombok.SneakyThrows;
import nl.hsleiden.AfkoAPI.dao.UserDetailsDAO;
import nl.hsleiden.AfkoAPI.httpResponses.Response;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Filter to read and validate JWT.
 * @author Daniel Paans
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserDetailsDAO USER_DETAILS_DAO;
    private final JwtUtil JWT_UTIL;

    public JwtRequestFilter(UserDetailsDAO userDetailsService, JwtUtil jwtUtil) {
        this.USER_DETAILS_DAO = userDetailsService;
        this.JWT_UTIL = jwtUtil;
    }

    /**
     * The filter functionality of extracting information from JWT and validating the JWT.
     * @author Daniel Paans
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String AUTHORIZATION_HEADER = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // Extract username from JWT token
        try {
            if (AUTHORIZATION_HEADER != null && AUTHORIZATION_HEADER.startsWith("Bearer ")) {
                jwt = AUTHORIZATION_HEADER.substring(7);
                username = JWT_UTIL.extractUsername(jwt);}
        } catch (SignatureException | MalformedJwtException | ExpiredJwtException |
                UnsupportedJwtException | IllegalArgumentException | IncorrectResultSizeDataAccessException e) {
            throwResponse(response, e);
            return;
        }

        // Validate JWT and set Spring security authentication for the UsernamePasswordAuthenticationFilter
        try {
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.USER_DETAILS_DAO.loadUserByUsername(username);
                if (JWT_UTIL.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        } catch(UsernameNotFoundException unfe) {
            throwResponse(response, unfe);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private void throwResponse(HttpServletResponse response, Exception e) throws IOException {
        String body = responseToJsonString(e);
        response.setContentType("application/json");
        response.setContentLength(body.length());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getOutputStream().println(body);
    }

    private String responseToJsonString(Exception e) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
        return mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(new Response(401, e.getMessage()));
    }
}
