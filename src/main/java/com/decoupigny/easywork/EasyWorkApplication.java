package com.decoupigny.easywork;

import com.decoupigny.easywork.services.FilesStoreService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.Resource;

@EnableWebMvc
@SpringBootApplication
public class EasyWorkApplication implements CommandLineRunner {
	@Resource
	FilesStoreService storageService;

	public static void main(String[] args) {
		SpringApplication.run(EasyWorkApplication.class, args);
	}

	@Override
	public void run(String... arg){
		storageService.init();
	}
}
