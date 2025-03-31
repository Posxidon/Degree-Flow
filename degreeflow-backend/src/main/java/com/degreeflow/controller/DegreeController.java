package com.degreeflow.controller;

import com.degreeflow.model.Degree;
import com.degreeflow.service.PathwayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/degree")
public class DegreeController {

    @Bean
    SecurityFilterChain web(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                        .anyRequest().authenticated())
                .httpBasic(withDefaults());

        return http.build();
    }
    //and cors configuration
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "OPTIONS"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Autowired
    private PathwayService pathwayService;

    @GetMapping("/requirement")
    public Degree gerRequirement(@RequestParam("degreeName") String degreeName) {
        System.out.println("param");
        System.out.println(degreeName);
        if (degreeName.length()>0){
            return pathwayService.parseDegreePlan(degreeName,false);
        }else {
            return pathwayService.parseDegreePlan("HCOMPSCICO",false);
        }
    }
    @GetMapping("/degreeName")
    public List<List<String>> getAllDegree() {
        return pathwayService.printCodes();
    }
}
