package mtg.priceUpdater;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.actuate.system.ApplicationPidFileWriter;

import javax.annotation.PreDestroy;

@SpringBootApplication
@EnableEurekaClient
public class MtgPriceUpdaterApplication {

	public static void main(String[] args) {
		SpringApplication notificationMicroService = new SpringApplication(MtgPriceUpdaterApplication.class);
		notificationMicroService.addListeners(new ApplicationPidFileWriter("notification-micro-service.pid"));
		notificationMicroService.run(args);
	}
}
