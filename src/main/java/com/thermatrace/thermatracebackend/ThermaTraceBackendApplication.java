package com.thermatrace.thermatracebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ThermaTraceBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThermaTraceBackendApplication.class, args);
    }

}
