package io.javabrain.aichat.match;

import io.javabrain.aichat.conversations.Conversation;
import io.javabrain.aichat.conversations.ConversationRepository;
import io.javabrain.aichat.profiles.Profile;
import io.javabrain.aichat.profiles.ProfileRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class MatchController {


    private final ProfileRepository profileRepo;
    private final ConversationRepository conversationRepo;
    private final MatchRepository matchRepo;

    public MatchController(ProfileRepository profileRepo, ConversationRepository conversationRepo, MatchRepository matchRepo) {
        this.profileRepo = profileRepo;
        this.conversationRepo = conversationRepo;
        this.matchRepo = matchRepo;
    }

    @PostMapping("/matches")
    @CrossOrigin(origins = "*")
    public Match createMatch(@RequestBody CreateMatchRequest request) {
        Profile profile = profileRepo.findById(request.profileId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found"));
        //todo ensure no existing conversation
        Conversation convention = new Conversation(UUID.randomUUID().toString(), profile.id(), new ArrayList<>());
        conversationRepo.save(convention);
        Match match = new Match(UUID.randomUUID().toString(), profile, convention.id());
        matchRepo.save(match);
        return match;
    }

    @GetMapping("/matches")
    @CrossOrigin(origins = "*")
    public List<Match> getAllMatches() {
        return matchRepo.findAll();
    }

    public record CreateMatchRequest(String profileId) {

    }
}
