package mtg.priceUpdater;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Created by jbo on 21.06.2016.
 */
@Controller
public class PriceController {

    private static final String PRICE_UPDATE_TEMPLATE  = "priceUpdate";

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Autowired
    private PriceUpdatePublisher priceUpdatePublisher;

    @RequestMapping(value="/", method = RequestMethod.GET)
    public String sendPriceUpdate(Model model) {
        model.addAttribute("update", createDefaultPriceUpdate());
        return PRICE_UPDATE_TEMPLATE;
    }

    private PriceUpdate createDefaultPriceUpdate() {

        Price price = new Price();
        price.setLow(10);
        price.setMedian(20);
        price.setHigh(40);

        PriceUpdate priceUpdate = new PriceUpdate();
        priceUpdate.setCardId("abduction");
        priceUpdate.setEditionId("WTH");
        priceUpdate.setPrice(price);

        return priceUpdate;
    }

    @RequestMapping(value="/", method = RequestMethod.POST)
    public String addCardToCollection(@ModelAttribute PriceUpdate update, Model model) throws JsonProcessingException {
        priceUpdatePublisher.publishPriceUpdate(update);
        model.addAttribute("update", new PriceUpdate());
        return PRICE_UPDATE_TEMPLATE;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value="/purge", method = RequestMethod.GET)
    public void purgeUpdateQueue() {
        Stream
                .of("queue-mtg-price-update-mtg-my-card-collection", "queue-mtg-price-update-mtg-card-catalogue")
                .forEach(queue -> {
                    amqpAdmin.purgeQueue(queue, false);
                    Logger.getGlobal().info("Message queue " + queue + " purged");
                });
    }
}
