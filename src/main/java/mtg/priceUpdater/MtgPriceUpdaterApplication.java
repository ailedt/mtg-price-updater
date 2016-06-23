package mtg.priceUpdater;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MtgPriceUpdaterApplication {

	public static void main(String[] args) {
		new SpringApplication(MtgPriceUpdaterApplication.class).run(args);
	}
}
