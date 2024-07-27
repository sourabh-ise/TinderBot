package io.javabrain.aichat.conversations;

import io.javabrain.aichat.profiles.ProfileRepository;
import io.javabrain.aichat.profiles.Profile;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class ConversationController {

    private final ConversationRepository conversationRepository;
    private final ProfileRepository profileRepository;
    private final ConversationService conversationService;

    public ConversationController(ConversationRepository conversationRepository, ProfileRepository profileRepository,
        ConversationService conversationService) {
        this.conversationRepository = conversationRepository;
        this.profileRepository = profileRepository;
        this.conversationService = conversationService;
    }


    @CrossOrigin(origins = "*")
    @PostMapping("/conversations/{conversationId}")
    public Conversation createConversationRequest(@PathVariable String conversationId, @RequestBody ChatMessage message) {
        Conversation conversation = conversationRepository.findById(conversationId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, STR."Conversation not found\{conversationId}"));

        String matchProfileID = conversation.profileId();
        Profile profile = profileRepository.findById(matchProfileID)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found"));
        Profile user = profileRepository.findById(message.authorId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found"));
        //ensure that the author of the message is the same as the author of the conversation or the receiver of the conversation
        ChatMessage messageWithTime = new ChatMessage(message.messageText(), user.id(), LocalDateTime.now());
        conversation.messages().add(messageWithTime);
        conversationService.generateProfileResponse(conversation, profile, user);

        conversationRepository.save(conversation);
        return conversation;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/conversations/{conversationId}")
    public Conversation getConversation(@PathVariable String conversationId) {
        return conversationRepository.findById(conversationId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, STR."Conversation not found\{conversationId}"));
    }
}
