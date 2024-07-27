package io.javabrain.aichat.match;

import io.javabrain.aichat.profiles.Profile;

public record Match(String id, Profile profile, String conversationId) {

}
