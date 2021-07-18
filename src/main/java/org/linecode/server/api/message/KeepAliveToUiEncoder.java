/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source licence (see accompanying file LICENCE).
 */

package org.linecode.server.api.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Inject;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class KeepAliveToUiEncoder implements Encoder.Text<KeepAliveToUi> {
    @Inject
    private static ObjectMapper mapper;

    @Override
    public String encode(KeepAliveToUi keepAliveToUi) throws EncodeException {
        String message;
        try {
            message = mapper.writeValueAsString(keepAliveToUi);
        } catch (JsonProcessingException e) {
            throw new EncodeException(keepAliveToUi, "ObjectMapper thrown an error while processing KeepAliveToUi", e);
        }
        return message;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
        // Override requested but no operations needed
    }

    @Override
    public void destroy() {
        // Override requested but no operations needed
    }
}
