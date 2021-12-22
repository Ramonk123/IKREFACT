package nl.hsleiden.AfkoAPI.configurations;

import nl.hsleiden.AfkoAPI.JwtRequestFilter;
import nl.hsleiden.AfkoAPI.dao.UserDetailsDAO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;

import java.util.Arrays;
import java.util.List;

/**
 * @author Daniel Paans
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {


    private final UserDetailsDAO USER_DETAILS_DAO;
    private final JwtRequestFilter JWT_REQUEST_FILTER;

    public SecurityConfiguration(UserDetailsDAO userDetailsService, JwtRequestFilter jwtRequestFilter) {
        this.USER_DETAILS_DAO = userDetailsService;
        this.JWT_REQUEST_FILTER = jwtRequestFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(USER_DETAILS_DAO);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        final String PATH = "/api";
        final String ADMIN = "ADMIN";
        final String AUTHORIZED_USER = "AUTHORIZED";
        http.cors().and().csrf().disable()
                .authorizeRequests()
                //TODO: remove comments in final
//                    .antMatchers(PATH+"/admin/**").hasRole(ADMIN)
//                    .antMatchers(HttpMethod.POST, PATH+"/departments/**").hasRole(ADMIN)
//                    .antMatchers(HttpMethod.DELETE, PATH+"/reports/**").hasRole(ADMIN)
//                    .antMatchers(HttpMethod.POST, PATH+"/abbreviations/**").hasAnyRole(ADMIN, AUTHORIZED_USER)
//                    .antMatchers(HttpMethod.GET, PATH+"/reports/**").hasAnyRole(ADMIN, AUTHORIZED_USER)
//                    .antMatchers(HttpMethod.GET, PATH+"/abbreviations/**").permitAll()
//                    .antMatchers(HttpMethod.POST, PATH+"/reports/**").permitAll()
//                    .antMatchers(PATH+"/authorizedUser/**", PATH+"/authenticate/**", PATH+"/admin/**", PATH+"/user/**").permitAll()
                // and remove this ↓ line in final
//                    .antMatchers(PATH, PATH+"/authenticate/**", PATH+"/departments/**",
//                                                PATH+"/abbreviations/**", PATH+"/game/**", PATH+"/reports/**",
//                                                PATH+"/authorizedUser/**", PATH+"/user/**", PATH+"/admin/**").permitAll()
                .anyRequest().anonymous()

                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(JWT_REQUEST_FILTER, UsernamePasswordAuthenticationFilter.class);
    }

   @Override
   @Bean
   public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
   }

   @Bean
   public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
   }
}
