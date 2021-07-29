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

public class UnitPositionToUiEncoder implements Encoder.Text<UnitPositionToUi> {
    @Inject
    private static ObjectMapper mapper;

    @Override
    public String encode(UnitPositionToUi unitPositionToUi) throws EncodeException {
        String message;
        try {
            message = mapper.writeValueAsString(unitPositionToUi);
        } catch (JsonProcessingException e) {
            throw new EncodeException(
                    unitPositionToUi,
                    "ObjectMapper thrown an error while processing UnitPositionToUi",
                    e
            );
        }
        return message;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {}

    @Override
    public void destroy() {}
}
