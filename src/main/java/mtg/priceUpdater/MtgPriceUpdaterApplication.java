package mtg.priceUpdater;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MtgPriceUpdaterApplication implements CommandLineRunner{

	private final static String QUEUE = "mtg-price-update";
	private final static String PRICE_UPDATE_ABDUCTION = "{\n" +
			"\t\"card-id\" : \"abduction\",\n" +
			"\t\"edition-id\" : \"WTH\",\n" +
			"\t\"price\" : {\n" +
			"\t\t\"low\" : 1,\n" +
			"\t\t\"median\" : 6,\n" +
			"\t\t\"high\" : 14\n" +
			"    }\n" +
			"}";

	@Autowired
	AnnotationConfigApplicationContext context;

	@Autowired
	RabbitTemplate rabbitTemplate;

	@Bean
	Queue queue() {
		return new Queue(QUEUE, false);
	}

	@Bean
	TopicExchange exchange() {
		return new TopicExchange("mtg-exchange");
	}

	@Bean
	Binding binding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(QUEUE);
	}

	public static void main(String[] args) {
		SpringApplication.run(MtgPriceUpdaterApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Sending message...");
		rabbitTemplate.convertAndSend(QUEUE, PRICE_UPDATE_ABDUCTION);
		context.close();
	}
}
