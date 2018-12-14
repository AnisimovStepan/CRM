package com.ewp.crm.service.conversation;

import com.ewp.crm.configs.VKConfigImpl;
import com.ewp.crm.configs.inteface.VKConfig;
import com.ewp.crm.service.interfaces.VKService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class JMVkConversation implements JMConversation {

    //constant string
    private final String fldUserName    = "first_name";
    private final String fldLastName    = "last_name";
    private final String fldUserNameIns = "first_name_ins";
    private final String fldLastNameIns = "last_name_ins";
    private final String fldName        = "name";

    private final String fldPhotoSize   = "photo_50";
    private final String addQueryFld    = "photo_50,first_name_ins, last_name_ins";
    private final String defaultVkPhoto = "https://vk.com/images/camera_50.png?ava=1";

    //main fields
//    private String groupId;
//    private String accessToken;
//    private String version;
//    private String url;

    private Interlocutor interlocutor;
    private Interlocutor group;

    private int rev = 0; //порядок сортировки сообщений {0,1}
    private int maxMessageCount;

    private List<ChatMessage> messages;
    private List<ChatMessage> unreadMessages;

    private final VKConfig vkConfig;
    private final VKService vkService;

    @Autowired
    public JMVkConversation(VKConfigImpl vkConfig, VKService vkService) {
        this.vkConfig = vkConfig;
        this.vkService = vkService;

        //вытаскиваем параметры группы
        //group = new Interlocutor(groupId, "");
    }

    @Override
    public void dropChat() {
        if (messages != null) {
            messages.clear();
        } else {
            messages = new LinkedList<>();
        }

        if (unreadMessages != null) {
            unreadMessages.clear();
        } else {
            unreadMessages = new LinkedList<>();
        }

        //удаляем собеседника
        interlocutor = null;
    }

    @Override
    public ChatType getChatTypeOfConversation() {
        return ChatType.vk;
    }

    @Override
    public void startNewChat(String id) {

        dropChat();

        //Получаем данные собеседника
        //interlocutor = new Interlocutor(id, "");
    }

    @Override
    public ChatMessage sendMessage(String text) {
        return null;
    }

    @Override
    public ChatMessage markMessageAsRead(ChatMessage message) {
        return null;
    }

    @Override
    public List<ChatMessage> getMessages(int count) {
        return null;
    }

    @Override
    public List<ChatMessage> getNewMessages() {
        return null;
    }
}