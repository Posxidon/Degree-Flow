package com.degreeflow.controller;

import com.degreeflow.model.Degree;
import com.degreeflow.service.PathwayService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import netscape.javascript.JSObject;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import java.util.Objects;

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

    /**
     * api endpoint for get request to get requirement of certain degree
     * @param degreeName - the degree code of the degree to be fetched
     * @param showTech - whether to list technical electives or not
     * @return - degree object representing the requirements of the degree
     */
    @GetMapping("/requirement")
    public Degree gerRequirement(@RequestParam("degreeName") String degreeName, @RequestParam("showTech") String showTech) {
        System.out.println("param");
        System.out.println(degreeName);
        System.out.println("showTech");
        System.out.println(showTech);
        boolean showParam = Objects.equals(showTech, "true");
        if (degreeName.length()>0){
            return pathwayService.parseDegreePlan(degreeName,showParam);
        }else {
            return pathwayService.parseDegreePlan("HCOMPSCICO",showParam);
        }
    }

    /**
     * api endpoint for get list of all degree names and codes
     * @return an ordered list of all degree names and codes
     */
    @GetMapping("/degreeName")
    public List<List<String>> getAllDegree() {
        return pathwayService.printCodes();
    }

    /**
     * post user schedule to db
     * @param schedule - schedule json to be posted
     * @return - http response indicating success status
     */
    @PostMapping("/addSchedule")
    public ResponseEntity<?> addSchedule(@RequestBody String schedule) {
        System.out.println("schedule");
        System.out.println(schedule);
        String id = "1";
        if (pathwayService.addToDB(schedule,id)) {
            return ResponseEntity.ok("success");
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("failed db");
    }
}
