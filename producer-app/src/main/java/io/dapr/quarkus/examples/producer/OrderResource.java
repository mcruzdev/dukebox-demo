package io.dapr.quarkus.examples.producer;

import io.dapr.client.DaprClient;
import io.dapr.client.domain.ExecuteStateTransactionRequest;
import io.dapr.client.domain.State;
import io.dapr.client.domain.TransactionalStateOperation;
import io.dapr.client.domain.TransactionalStateOperation.OperationType;
import io.quarkus.logging.Log;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Path("/orders")
public class OrderResource {

    final DaprClient daprClient;

    public OrderResource(DaprClient daprClient) {
        this.daprClient = daprClient;
    }

    // add a retry using jobs...

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


        // save both using an tx

        // after i will send a message to message broker

        // finally
        // delete the outbox line from my database
 

        Log.info("Storing Order: " + order);
        this.daprClient.saveState("kvstore", order.getId(), order).block();
        Log.info("Publishing Order Event: " + order);
        this.daprClient.publishEvent("pubsub", "topic", order).block();
        return "Order Stored and Event Published";
    }



































    @POST
    @Path("/outbox")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String outboxStoreOrder(Order order) {

        Log.info("Storing Order: " + order);

        ExecuteStateTransactionRequest executeStateTransactionRequest = new ExecuteStateTransactionRequest(
                "postgresql-outbox");

        List<TransactionalStateOperation<?>> ops = new ArrayList<>();

        State<Order> state = new State<>(order.getId(), order, null);

        TransactionalStateOperation<Order> upsertOps = new TransactionalStateOperation<>(OperationType.UPSERT, state);

        ops.add(upsertOps);

        executeStateTransactionRequest.setOperations(ops);

        this.daprClient.executeStateTransaction(executeStateTransactionRequest).block();

        return "Order Stored and Event Published (via Outbox Pattern)";
    }

    @GET
    @Path("/{orderId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findById(@PathParam("orderId") String orderId, @QueryParam("storeName") String storeName) {

        Log.info("Finding Order by ID: " + orderId);
        
        State<Order> orderState = this.daprClient.getState(storeName, orderId, Order.class)
                .block();

        if (Objects.isNull(orderState.getValue())) {
            return Response.status(Status.NOT_FOUND).build();
        }

        return Response.ok(orderState.getValue()).build();
    }
}
