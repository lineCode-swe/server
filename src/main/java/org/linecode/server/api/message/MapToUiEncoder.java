/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright (c) lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source license (see accompanying file LICENSE).
 ******************************************************************************/

package org.linecode.server.api.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Inject;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class MapToUiEncoder implements Encoder.Text<MapToUi> {
    @Inject
    private static ObjectMapper mapper;

    @Override
    public String encode(MapToUi mapToUi) throws EncodeException {
        String message;
        try {
            message = mapper.writeValueAsString(mapToUi);
        } catch (JsonProcessingException e) {
            throw new EncodeException(mapToUi, "ObjectMapper thrown an error while processing MapToUi", e);
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
