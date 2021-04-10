package dev.itatyo.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    private ClerkMapper clerkmapper;
    public DemoApplication (ClerkMapper clerkMapper) {
        this.clerkmapper = clerkMapper;
    }
    @Bean
    CommandLineRunner sampleCommandLineRunner() {
        return args -> System.out.println(this.clerkmapper.findByID(1));
    }
}
