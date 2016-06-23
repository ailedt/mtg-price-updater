package mtg.priceUpdater;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jbo on 23.06.2016.
 */
@Component
public class PriceUpdatePublisher {

    private final static String TOPIC = "topic-mtg-price-updates";
    private final static String ROUTING_KEY = "card.price.update";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void publishPriceUpdate(PriceUpdate update) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String priceUpdateAsJsonString = objectMapper.writerFor(PriceUpdate.class).writeValueAsString(update);
            rabbitTemplate.convertAndSend(TOPIC, ROUTING_KEY, priceUpdateAsJsonString);
        } catch (JsonProcessingException e) {
            Logger.getGlobal().log(Level.WARNING, "Could not publish price update", e);
        }
    }
}
