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

	public final static String QUEUE = "mtg-price-update";

	@Autowired
	private ConfigurableApplicationContext context;

	@Bean
	public Queue queue() {
		return new Queue(QUEUE, false);
	}

	@Bean
	public TopicExchange exchange() {
		return new TopicExchange("mtg-exchange");
	}

	@Bean
	public Binding binding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(QUEUE);
	}

	public static void main(String[] args) {
		SpringApplication notificationMicroService = new SpringApplication(MtgPriceUpdaterApplication.class);
		notificationMicroService.addListeners(new ApplicationPidFileWriter("notification-micro-service.pid"));
		notificationMicroService.run(args);
	}

	@PreDestroy
	public void cleanUp() {
		context.close();
	}
}
