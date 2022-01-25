package com.decoupigny.easywork.repository;

import com.decoupigny.easywork.models.messenger.ChatMessage;
import com.decoupigny.easywork.models.messenger.MessageStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ChatMessageRepository
        extends MongoRepository<ChatMessage, String> {

    long countBySenderIdAndRecipientIdAndStatus(
            String senderId, String recipientId, MessageStatus status);

    List<ChatMessage> findByChatId(String chatId, Sort sort);
}