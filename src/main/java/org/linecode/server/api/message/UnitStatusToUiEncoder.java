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

public class UnitStatusToUiEncoder implements Encoder.Text<UnitStatusToUi> {
    @Inject
    private static ObjectMapper mapper;

    @Override
    public String encode(UnitStatusToUi unitStatusToUi) throws EncodeException {
        String message;
        try {
            message = mapper.writeValueAsString(unitStatusToUi);
        } catch (JsonProcessingException e) {
            throw new EncodeException(
                    unitStatusToUi,
                    "ObjectMapper thrown an error while processing UnitStatusToUi",
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
