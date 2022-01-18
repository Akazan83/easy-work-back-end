package com.decoupigny.easywork;

import com.decoupigny.easywork.models.messenger.ChatMessage;
import com.decoupigny.easywork.models.messenger.ChatNotification;
import com.decoupigny.easywork.models.messenger.MessageStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

@SpringBootApplication
public class EasyWorkApplication {

	private ChatNotification notif = ChatNotification.builder()
			.id("61db5a373240426ebbf177f3_61db59e13240426ebbf177f2")
			.senderId("61db59e13240426ebbf177f2")
			.senderName("Bob")
			.build();

	@Autowired
	private SimpMessageSendingOperations messagingTemplate;


	public static void main(String[] args) {
		SpringApplication.run(EasyWorkApplication.class, args);
	}

	/**
	 * Generate random numbers publish with WebSocket protocol each 3 seconds.
	 * @return a command line runner.
	 */
	@Bean
	public CommandLineRunner websocketDemo() {
		return (args) -> {
			while (true) {
				try {
					Thread.sleep(3*1000); // Each 3 sec.
					//messagingTemplate.convertAndSend("/topic/progress", this.progress);
					messagingTemplate.convertAndSendToUser("61db59e13240426ebbf177f2","/queue/messages", this.notif);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
	}

	/**
	 * Get a random integer value in a min / max range.
	 * @param min min range value
	 * @param max max range value
	 * @return A random integer value
	 */
	private int randomWithRange(int min, int max)
	{
		int range = Math.abs(max - min) + 1;
		return (int)(Math.random() * range) + (min <= max ? min : max);
	}

}
