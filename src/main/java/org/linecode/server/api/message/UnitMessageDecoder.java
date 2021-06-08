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
import org.linecode.server.Position;
import org.linecode.server.business.AuthStatus;
import org.linecode.server.business.UnitStatus;

import javax.inject.Inject;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class UnitMessageDecoder implements Decoder.Text<Message> {

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
            case "PositionToServer":
                return new PositionFromUnit(
                        node.path("id").asText(),
                        new Position(node.path("position").path("x").asInt(),node.path("position").path("y").asInt())
                );

            case "ErrorToServer":
                return new ErrorFromUnit(
                        node.path("id").asText(),
                        node.path("error").asInt()
                );

            case "ObstacleListToServer":
                List<Position> temporal = new ArrayList<Position>();
                Consumer<JsonNode> data = (JsonNode subnode) -> temporal.add(new Position(subnode.path("x").asInt()
                        ,subnode.path("y").asInt()));
                node.path("position").forEach(data);
                return new ObstacleListFromUnit(
                       node.path("id").asText(), temporal
                );

            case "PathRequestToServer":

                return new PathRequestFromUnit(
                        node.path("id").asText()
                );

            case "SpeedToServer":

                return new SpeedFromUnit(
                        node.path("id").asText(),
                        node.path("speed").asInt()
                );

            case "StatusToServer":

                return new StatusFromUnit(
                        node.path("id").asText(),
                        UnitStatus.valueOf(node.path("status").asText())
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
            case "PositionToServer":
                return !(
                        node.path("id").isMissingNode() ||
                                node.path("position").isMissingNode()
                );

            case "ErrorToServer":
                return !(
                        node.path("id").isMissingNode() || node.path("err").isMissingNode()
                );

            case "ObstacleListToServer":
                return !(
                        node.path("id").isMissingNode() ||
                                node.path("obstacles").isMissingNode()
                );

            case "PathRequestToServer":

                return !(
                        node.path("id").isMissingNode() ||
                                node.path("pathRequest").isMissingNode()
                );

            case "SpeedToServer":

                return !(
                        node.path("id").isMissingNode() ||
                                node.path("speed").isMissingNode()
                );

            case "StatusToServer":
                return !(
                        node.path("id").isMissingNode() ||
                                node.path("status").isMissingNode()
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
