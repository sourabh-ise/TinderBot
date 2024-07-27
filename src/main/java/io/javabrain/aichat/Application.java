package io.javabrain.aichat;

import io.javabrain.aichat.profiles.ProfileCreationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {

    ProfileCreationService profileCreationService;

    public Application(final ProfileCreationService profileCreationService) {
        this.profileCreationService = profileCreationService;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(final String... args) throws Exception {
        profileCreationService.saveProfilesToDB();
    }
}
