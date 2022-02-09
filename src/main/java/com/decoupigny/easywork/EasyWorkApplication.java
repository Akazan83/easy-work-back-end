package com.decoupigny.easywork;

import com.decoupigny.easywork.models.messenger.Notification;
import com.decoupigny.easywork.services.FilesStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import javax.annotation.Resource;
import java.util.Date;

@SpringBootApplication
public class EasyWorkApplication implements CommandLineRunner {
	@Resource
	FilesStoreService storageService;

	private final Notification notif = Notification.builder()
			.id("61dc675f1e2a3602b7a4de2d")
			.senderId("61db5a373240426ebbf177f3")
			.senderName("Bob")
			.type("NewTicket")
			.creationDate(new Date())
			.build();

	@Autowired
	private SimpMessageSendingOperations messagingTemplate;


	public static void main(String[] args) {
		SpringApplication.run(EasyWorkApplication.class, args);
	}

	@Override
	public void run(String... arg) throws Exception {
		storageService.deleteAll();
		storageService.init();
	}

	/**
	 * Generate random numbers publish with WebSocket protocol each 3 seconds.
	 * @return a command line runner.
	 */
	/*@Bean
	public CommandLineRunner websocketDemo() {
		return (args) -> {
			while (true) {
				try {
					Thread.sleep(10*1000); // Each 3 sec.
					//messagingTemplate.convertAndSend("/topic/progress", this.progress);
					System.out.println(this.notif);
					messagingTemplate.convertAndSendToUser("61db59e13240426ebbf177f2","/queue/messages", this.notif);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
	}*/


}
