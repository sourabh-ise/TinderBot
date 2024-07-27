package io.javabrain.aichat.conversations;

import io.javabrain.aichat.profiles.Profile;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.ai.chat.messages.AbstractMessage;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConversationService {

    @Autowired
    OllamaChatModel chatModel;

    public void generateProfileResponse(Conversation conversation, Profile profile, Profile user) {
        SystemMessage systemMessage = getSystemMessage(profile, user);

        List<AbstractMessage> conversationMessages = conversation.messages().stream().map(message -> {
            if (message.authorId().equals(profile.id())) {
                return new AssistantMessage(message.messageText());
            } else {
                return new UserMessage(message.messageText());
            }
        }).toList();
        List<Message> allMessages = new ArrayList<>();
        allMessages.add(systemMessage);
        allMessages.addAll(conversationMessages);
        
        Prompt prompt = new Prompt(allMessages);
        ChatResponse response = chatModel.call(prompt);
        conversation.messages().add(new ChatMessage(response.getResult().getOutput().getContent(), profile.id(), LocalDateTime.now()));
    }

    private static SystemMessage getSystemMessage(final Profile profile, final Profile user) {
        String systemMessageStr = STR."""
            Your are a \{profile.age()} year old \{profile.ethnicity()} \{profile.gender()} called \{profile.firstName()} \{profile.lastName()}
            with bio \{profile.bio()} and myers briggs personality type is \{profile.myersBriggsPersonalityType()}
            matched with a \{user.age()} year old \{user.ethnicity()} \{user.gender()} called \{user.firstName()} \{user.lastName()}
            with bio \{user.bio()} and myers briggs personality type is \{user.myersBriggsPersonalityType()} on tinder.
            This is an in-app text conversation between you two. 
            Pretend to be the described persaon and respond to the conversation based on your profile, personality type as if writing on 
            tinder.
            Keep your responses short, smart, engaging and ensure not to mention anything about you being an AI or chatbot.
            """;
        return new SystemMessage(systemMessageStr);
    }
}