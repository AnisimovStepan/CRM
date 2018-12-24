package com.ewp.crm.service.conversation;

import com.ewp.crm.models.Client;
import com.ewp.crm.service.interfaces.SocialProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class JMConversationHelperImpl implements JMConversationHelper {

    private final int CHAT_MESSAGE_LIMIT = 40;

    private final List<JMConversation> conversations;
    private final SocialProfileService socialProfileService;

    @Autowired
    public JMConversationHelperImpl(List<JMConversation> conversations, SocialProfileService socialProfileService) {
        this.conversations = conversations;
        this.socialProfileService = socialProfileService;
    }

    @Override
    public void endChat(Client client) {
        for (JMConversation conversation: conversations) {
            conversation.endChat(client);
        }
    }

    @Override
    public ChatMessage sendMessage(ChatMessage message) {
        for (JMConversation conversation: conversations) {
            if (message.getChatType() == conversation.getChatTypeOfConversation()) {
                return conversation.sendMessage(message);
            }
        }
        return message;
    }

    @Override
    public Map<Client, Integer> getCountOfNewMessages() {
        Map<Client, Integer> clientMap = new HashMap<>();
        for (JMConversation conversation: conversations) {
            Map<Client, Integer> newMessageFromConversation = conversation.getCountOfNewMessages();
            for(Map.Entry<Client, Integer> element: newMessageFromConversation.entrySet()){
                Integer count = clientMap.getOrDefault(element.getKey(), 0);
                clientMap.put(element.getKey(), element.getValue()+count);
            }
        }
        return clientMap;
    }

    @Override
    public List<ChatMessage> getNewMessages(Client client) {
        List<ChatMessage> list = new LinkedList<>();
        for (JMConversation conversation: conversations) {
            List<ChatMessage> conversationMsg = conversation.getNewMessages(client, CHAT_MESSAGE_LIMIT);
            if (conversationMsg != null && !conversationMsg.isEmpty()) {
                list.addAll(0, conversationMsg);
            }
        }
        list.sort(Comparator.comparing(ChatMessage::getTime));
        return list;
    }

    @Override
    public List<ChatMessage> getMessages(Client client) {
        List<ChatMessage> list = new LinkedList<>();
        for (JMConversation conversation: conversations) {
            List<ChatMessage> conversationMsg = conversation.getMessages(client, CHAT_MESSAGE_LIMIT);
            if (conversationMsg != null && !conversationMsg.isEmpty()) {
                list.addAll(conversationMsg);
            }
        }
        list.sort(Comparator.comparing(ChatMessage::getTime));
        return list;
    }

    @Override
    public Map<ChatType, String> getReadMessages(Client client) {
        Map<ChatType, String> chatTypeStringMap = new HashMap<>();
        for (JMConversation conversation: conversations) {
            String lastMsg = conversation.getReadMessages(client);
            chatTypeStringMap.put(conversation.getChatTypeOfConversation(), lastMsg);
        }
        return chatTypeStringMap;
    }

    @Override
    public List<Interlocutor> getInterlocutors(Client client) {
        List<Interlocutor> list = new LinkedList<>();
        for (JMConversation conversation: conversations) {
            Optional<Interlocutor> interlocutor = conversation.getInterlocutor(client);
            interlocutor.ifPresent(list::add);
        }
        return list;
    }

    @Override
    public List<Interlocutor> getUs() {
        List<Interlocutor> list = new LinkedList<>();
        for (JMConversation conversation: conversations) {
            Optional<Interlocutor> interlocutor = conversation.getMe();
            interlocutor.ifPresent(list::add);
        }
        return list;
    }

}
