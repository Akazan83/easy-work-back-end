package com.decoupigny.easywork.controllers.messenger;


import com.decoupigny.easywork.models.messenger.Message;
import com.decoupigny.easywork.models.messenger.MessengerNotification;
import com.decoupigny.easywork.services.MessengerRoomService;
import com.decoupigny.easywork.services.MessengerService;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class MessengerController {

    @Autowired private SimpMessagingTemplate messagingTemplate;
    @Autowired private MessengerService chatMessageService;
    @Autowired private MessengerRoomService chatRoomService;

    @MessageMapping("/chat")
    public void processMessage(@Payload Message chatMessage) {
        Optional<String> chatId = chatRoomService
                .getChatId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true);
        chatMessage.setChatId(chatId.get());

        Message saved = chatMessageService.save(chatMessage);
        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId(),"/queue/messages",
                new MessengerNotification(
                        saved.getId(),
                        saved.getSenderId(),
                        saved.getSenderName()));
    }

    @GetMapping("/messages/{senderId}/{recipientId}/count")
    public ResponseEntity<Long> countNewMessages(
            @PathVariable String senderId,
            @PathVariable String recipientId) {

        return ResponseEntity
                .ok(chatMessageService.countNewMessages(senderId, recipientId));
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<?> findChatMessages ( @PathVariable String senderId,
                                                @PathVariable String recipientId) {
        return ResponseEntity
                .ok(chatMessageService.findChatMessages(senderId, recipientId));
    }

    @GetMapping("/messages/{id}")
    public ResponseEntity<?> findMessage ( @PathVariable String id) {
        return ResponseEntity
                .ok(chatMessageService.findById(id));
    }
}