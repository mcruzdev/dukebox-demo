package io.dapr.quarkus.examples.producer;

import java.util.ArrayList;
import java.util.List;

import io.dapr.client.DaprClient;
import io.dapr.client.domain.ExecuteStateTransactionRequest;
import io.dapr.client.domain.State;
import io.dapr.client.domain.TransactionalStateOperation;
import io.dapr.client.domain.TransactionalStateOperation.OperationType;
import io.quarkus.logging.Log;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/outbox/orders")
public class OrderResourceOutbox {

    final DaprClient daprClient;

    public OrderResourceOutbox(DaprClient daprClient) {
        this.daprClient = daprClient;
    }

    @POST
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
}
