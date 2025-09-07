package io.dapr.quarkus.examples.producer;

import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.POST;

@RegisterRestClient(configKey = "dapr-api")
public interface DaprApi {

    @POST
    @ClientHeaderParam(name = "dapr-app-id", required = true, value = "consumer-app")
    @ClientHeaderParam(name = "Content-Type", required = true, value = "text/plain")
    String ping();

}
