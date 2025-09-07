package io.dapr.quarkus.examples.producer;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/ping")
public class PingResource {

    @Inject
    @RestClient
    DaprApi consumerRestClient;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String ping() {
        Log.info("Calling Dapr sidecar");
        String response = this.consumerRestClient.ping();
        Log.info("Response from consumer-app: " + response);
        return response;
    }

}