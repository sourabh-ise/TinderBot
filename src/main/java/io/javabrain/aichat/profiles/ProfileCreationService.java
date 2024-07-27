package io.javabrain.aichat.profiles;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javabrain.aichat.profiles.Profile.Gender;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class ProfileCreationService {

    private static final String PROFILES_FILE_PATH = "profiles.json";


    private final ProfileRepository profileRepository;

    @Value("#{${tinderai.character.user}}")
    private Map<String, String> userProfileProperties;

    public ProfileCreationService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public void saveProfilesToDB() {
        ObjectMapper objectMapper = new ObjectMapper();
        ClassPathResource resource = new ClassPathResource(PROFILES_FILE_PATH);
        try (InputStream inputStream = resource.getInputStream()) {
            List<Profile> profiles = objectMapper.readValue(inputStream, new TypeReference<>() {
            });
            profileRepository.deleteAll();
            profileRepository.saveAll(profiles);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Profile profile = new Profile(userProfileProperties.get("id"), userProfileProperties.get("firstName"),
            userProfileProperties.get("lastName"), Integer.parseInt(userProfileProperties.get("age")),
            userProfileProperties.get("ethnicity"), Gender.valueOf(userProfileProperties.get("gender")), userProfileProperties.get("bio"),
            userProfileProperties.get("imageUrl"), userProfileProperties.get("myersBriggsPersonalityType"));
        profileRepository.save(profile);
    }
}
