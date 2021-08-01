/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright (c) lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under ISC license (see accompanying file LICENSE).
 ******************************************************************************/

package org.linecode.server.api.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Inject;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class ObstaclesToUiEncoder implements Encoder.Text<ObstaclesToUi> {
    @Inject
    private static ObjectMapper mapper;

    @Override
    public String encode(ObstaclesToUi obstaclesToUi) throws EncodeException {
        String message;
        try {
            message = mapper.writeValueAsString(obstaclesToUi);
        } catch (JsonProcessingException e) {
            throw new EncodeException(obstaclesToUi, "ObjectMapper thrown an error while processing ObstaclesToUi", e);
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
