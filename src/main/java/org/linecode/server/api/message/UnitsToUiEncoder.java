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

public class UnitsToUiEncoder implements Encoder.Text<UnitsToUi> {
    @Inject
    private static ObjectMapper mapper;

    @Override
    public String encode(UnitsToUi unitsToUi) throws EncodeException {
        String message;
        try {
            message = mapper.writeValueAsString(unitsToUi);
        } catch (JsonProcessingException e) {
            throw new EncodeException(unitsToUi, "ObjectMapper thrown an error while processing UnitsToUi", e);
        }
        return message;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {}

    @Override
    public void destroy() {}
}
