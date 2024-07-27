package io.javabrain.aichat.profiles;

public record Profile(String id, //since field name is id, @Id is not needed
                      String firstName, 
                      String lastName, 
                      int age, 
                      String ethnicity, 
                      Gender gender, 
                      String bio, 
                      String imageUrl,
                      String myersBriggsPersonalityType
) {
    enum Gender {
        MALE, FEMALE;
    }
}
