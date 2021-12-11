package nl.hsleiden.AfkoAPI.configurations;

import com.fasterxml.jackson.databind.DeserializationFeature;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.Arrays;
import java.util.List;

/**
 * @author Daniel Paans
 */
@Configuration
public class DefaultConfiguration implements InitializingBean {

    private final RequestMappingHandlerAdapter CONVERTER;

    @Autowired
    public DefaultConfiguration(RequestMappingHandlerAdapter converter) {
        this.CONVERTER = converter;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        configureJacksonToFailOnUnknownProperties();
    }

    private void configureJacksonToFailOnUnknownProperties() {
        MappingJackson2HttpMessageConverter httpMessageConverter = CONVERTER.getMessageConverters().stream()
                .filter(mc -> mc.getClass()
                        .equals(MappingJackson2HttpMessageConverter.class))
                .map(mc -> (MappingJackson2HttpMessageConverter) mc)
                .findFirst()
                .get();



        httpMessageConverter.getObjectMapper().enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }
}