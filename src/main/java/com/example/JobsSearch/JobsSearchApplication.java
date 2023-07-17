package com.example.JobsSearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class JobsSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobsSearchApplication.class, args);
    }

}
