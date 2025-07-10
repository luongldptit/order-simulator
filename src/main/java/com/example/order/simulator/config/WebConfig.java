package com.example.order.simulator.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.concurrent.Executors;

@Configuration
@EnableWebSecurity
@Slf4j
public class WebConfig {

    @Value("#{'${web.urls.ignore}'.split(',')}")
    private String[] whileList;


    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return new ObjectMapper().registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .configure(JsonReadFeature.ALLOW_TRAILING_COMMA.mappedFeature(), true)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(whileList).permitAll()
                                .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .anonymous(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails admin = User.withUsername("admin")
                .password("{noop}123456")
                .roles("ADMIN")
                .build();
        UserDetails employee = User.withUsername("employee")
                .password("{noop}123456")
                .roles("EMPLOYEE")
                .build();

        return new InMemoryUserDetailsManager(admin, employee);
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info().title("Order Simulator API")
                .description("API for simulating order processing")
                .version("1.0")
                .license(new License().name("License of API")));
    }

    @Bean
    public TaskExecutor taskExecutor() {
        return new TaskExecutorAdapter(Executors.newVirtualThreadPerTaskExecutor());
    }

    @Bean
    public TomcatProtocolHandlerCustomizer<?> protocolHandlerVirtualThreadExecutor() {
        return protocolHandler ->
                protocolHandler.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
    }
}
