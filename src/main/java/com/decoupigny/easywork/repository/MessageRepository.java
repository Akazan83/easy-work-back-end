package com.decoupigny.easywork.repository;

import com.decoupigny.easywork.models.messenger.Message;
import com.decoupigny.easywork.models.messenger.MessageStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {

    long countBySenderIdAndRecipientIdAndStatus(
            String senderId, String recipientId, MessageStatus status);

    List<Message> findByChatId(String chatId);
}