package io.dapr.quarkus.examples.producer;

import io.dapr.client.DaprClient;
import io.quarkus.logging.Log;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/orders")
public class OrderResource {


    final DaprClient daprClient;

    public OrderResource(DaprClient daprClient) {
        this.daprClient = daprClient;
    }

    /**
     * Store orders from customers.
     *
     * @param order from the customer
     * @return confirmation that the order was stored and the event published
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String storeOrder(Order order) {
        Log.info("Storing Order: " + order);
        this.daprClient.saveState("kvstore", order.getId(), order).block();
        Log.info("Publishing Order Event: " + order);
        this.daprClient.publishEvent("pubsub", "topic", order).block();
        return "Order Stored and Event Published";
    }
}
