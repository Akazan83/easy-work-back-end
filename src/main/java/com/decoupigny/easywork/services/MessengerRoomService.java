package com.decoupigny.easywork.services;


import com.decoupigny.easywork.models.messenger.ChatRoom;
import com.decoupigny.easywork.repository.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MessengerRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    public Optional<String> getChatId(
            String senderId, String recipientId, boolean createIfNotExist) {

        // TODO: Refactoring
        return chatRoomRepository
                .findBySenderIdAndRecipientId(senderId, recipientId)
                .map(ChatRoom::getChatId);
               /* .or(() -> {
                    if(!createIfNotExist) {
                        return  Optional.empty();
                    }
                    String chatId =
                            String.format("%s_%s", senderId, recipientId);

                    ChatRoom senderRecipient = ChatRoom
                            .builder()
                            .chatId(chatId)
                            .senderId(senderId)
                            .recipientId(recipientId)
                            .build();

                    ChatRoom recipientSender = ChatRoom
                            .builder()
                            .chatId(chatId)
                            .senderId(recipientId)
                            .recipientId(senderId)
                            .build();
                    chatRoomRepository.save(senderRecipient);
                    chatRoomRepository.save(recipientSender);

                    return Optional.of(chatId);
                });*/
    }
}