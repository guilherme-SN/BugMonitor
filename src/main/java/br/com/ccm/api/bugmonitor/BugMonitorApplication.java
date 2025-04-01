package br.com.ccm.api.bugmonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BugMonitorApplication {

	public static void main(String[] args) {
		SpringApplication.run(BugMonitorApplication.class, args);
	}

}
