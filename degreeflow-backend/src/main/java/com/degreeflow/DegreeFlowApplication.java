package com.degreeflow;

import com.degreeflow.model.Schedule;
import com.degreeflow.repository.ScheduleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DegreeFlowApplication {
    public static void main(String[] args) {
        SpringApplication.run(DegreeFlowApplication.class, args);
    }

    @Bean
    public CommandLineRunner runner(ScheduleRepository scheduleRepository) {
        return args -> {
            System.out.println("Printing all rows from json_schedule:");
            for (Schedule s : scheduleRepository.findAll()) {
                System.out.println("User ID: " + s.getUserId());
                System.out.println("JSON data: " + s.getScheduleJson());
                System.out.println("----------------------------------------");
            }
        };
    }
}
