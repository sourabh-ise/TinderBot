package io.javabrain.aichat.profiles;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController {

    ProfileRepository profileRepository;

    public ProfileController(final ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @GetMapping("/profiles/random")
    @CrossOrigin(origins = "*") //allow all origins to access this endpoint
    public Profile getRandomProfile() {
        return profileRepository.getRandomProfile();
    }
}
