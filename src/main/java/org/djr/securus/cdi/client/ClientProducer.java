package org.djr.securus.cdi.client;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

/**
 * Created by djr4488 on 1/4/17.
 */
@ApplicationScoped
public class ClientProducer {
    @Produces
    public Client getClient(InjectionPoint ip) {
        return ClientBuilder.newClient();
    }
}
