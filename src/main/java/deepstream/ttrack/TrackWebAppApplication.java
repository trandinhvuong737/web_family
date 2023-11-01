package deepstream.ttrack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TrackWebAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrackWebAppApplication.class, args);
	}

}
