/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source licence (see accompanying file LICENCE).
 */

package org.linecode.server.api;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javax.inject.Inject;
import javax.websocket.server.ServerEndpointConfig;

public class EndpointConfigurator extends ServerEndpointConfig.Configurator {

    private final static Injector injector = Guice.createInjector(new EndpointModule());

    @Override
    public <T> T getEndpointInstance(Class<T> endpointClass) {
        return injector.getInstance(endpointClass);
    }
}
