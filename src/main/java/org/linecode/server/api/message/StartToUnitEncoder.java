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

public class StartToUnitEncoder implements Encoder.Text<StartToUnit> {
    @Inject
    private static ObjectMapper mapper;

    @Override
    public String encode(StartToUnit startToUnit) throws EncodeException {
        String message;
        try {
            message = mapper.writeValueAsString(startToUnit);
        } catch (JsonProcessingException e) {
            throw new EncodeException(startToUnit, "ObjectMapper thrown an error while processing StartToUnit", e);
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
