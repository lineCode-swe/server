/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source licence (see accompanying file LICENCE).
 */

package org.linecode.server.api.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Inject;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class MessageDecoder implements Decoder.Text<Message> {

    @Inject
    private static ObjectMapper mapper;

    @Override
    public Message decode(String s) throws DecodeException {
        JsonNode node;
        try {
            node = mapper.readTree(s);
        } catch (JsonProcessingException e) {
            throw new DecodeException(s, "Error in reading JSON", e);
        }

        switch (node.path("type").asText()) {
            case "LoginToServer":
                return new LoginFromUi(
                        node.path("user").asText(),
                        node.path("password").asText()
                );

            case "LogoutToServer":
                return new LogoutFromUi(
                        node.path("user").asText()
                );

            case "UserToServer":
                return new UserFromUi(
                        node.path("user").asText(),
                        node.path("password").asText(),
                        node.path("admin").asBoolean()
                );

            case "":
            default:
                throw new DecodeException(s, "Unrecognized type of message");
        }
    }

    @Override
    public boolean willDecode(String s) {
        JsonNode node;
        try {
            node = mapper.readTree(s);
        } catch (JsonProcessingException e) {
            return false;
        }

        switch (node.path("type").asText()) {
            case "LoginToServer":
                return !(
                        node.path("user").isMissingNode() ||
                        node.path("password").isMissingNode()
                );

            case "LogoutToServer":
                return !(
                        node.path("user").isMissingNode()
                );

            case "UserToServer":
                return !(
                        node.path("user").isMissingNode() ||
                        node.path("password").isMissingNode() ||
                        node.path("admin").isMissingNode()
                );

            case "":
            default:
                return false;
        }
    }

    @Override
    public void init(EndpointConfig endpointConfig) {}

    @Override
    public void destroy() {}
}
