package demo;

import com.hazelcast.config.Config;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public Config config() {
		return new Config("defaultInstance")
				.setProperty("hazelcast.jmx", "true");
	}

	@Bean
	public CommandLineRunner init(DemoRepository demoRepository) {
		return args -> {
			Demo demo = new Demo("demo");
			demoRepository.save(demo);
		};
	}

}
