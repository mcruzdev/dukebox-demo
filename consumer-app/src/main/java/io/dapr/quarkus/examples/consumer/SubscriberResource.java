package io.dapr.quarkus.examples.consumer;

import io.dapr.Topic;
import io.dapr.client.domain.CloudEvent;
import io.quarkus.logging.Log;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

@Path("/")
public class SubscriberResource {

    private List<CloudEvent<Order>> events = new ArrayList<>();

    @POST
    @Path("/subscribe")
    @Topic(pubsubName = "pubsub", name = "topic")
    public void subscribe(CloudEvent<Order> cloudEvent) {
        Log.info("Order Event Received: " + cloudEvent.getData());
        events.add(cloudEvent);
    }

    @GET
    @Path("/events")
    public Response getAllEvents() {
        return Response.ok(events).build();
    }

}
