package io.javabrain.aichat.profiles;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProfileRepository extends MongoRepository<Profile, String> {

    @Aggregation(pipeline = { "{ $sample: { size: 1 } }"}) //aggregate the profiles and return a random profile
    Profile getRandomProfile();
}
