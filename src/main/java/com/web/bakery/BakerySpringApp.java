package com.web.bakery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.web.bakery")
public class BakerySpringApp {

	public static void main(String[] args) {

		SpringApplication.run(BakerySpringApp.class, args);
		Thread backgroundThread = new Thread(() -> {
			while (true) {
				// Ваша основная логика здесь
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		backgroundThread.setDaemon(false); // Это предотвращает автоматическое завершение приложения, когда все незавершенные потоки являются демонами
		backgroundThread.start();
	}

}
