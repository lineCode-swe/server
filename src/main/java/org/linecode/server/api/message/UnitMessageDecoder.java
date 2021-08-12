/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright (c) lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under ISC license (see accompanying file LICENSE).
 ******************************************************************************/

package org.linecode.server.api.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.linecode.server.Position;
import org.linecode.server.business.UnitStatus;

import javax.inject.Inject;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.util.ArrayList;
import java.util.List;

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
                List<Position> obstacleList = new ArrayList<Position>();
                node.path("obstacles").forEach(poi -> obstacleList.add(new Position(
                        poi.path("x").asInt(),
                        poi.path("y").asInt())));
                return new PositionFromUnit(
                        new Position(
                                node.path("position").path("x").asInt(),
                                node.path("position").path("y").asInt()
                        ),
                        obstacleList);

            case "ErrorToServer":
                return new ErrorFromUnit(node.path("error").asInt());


            case "PathRequestToServer":

                return new PathRequestFromUnit();

            case "SpeedToServer":

                return new SpeedFromUnit(node.path("speed").asInt());

            case "StatusToServer":

                return new StatusFromUnit(UnitStatus.valueOf(node.path("status").asText()));

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
                return !(node.path("position").isMissingNode() || node.path("obstacles").isMissingNode());

            case "ErrorToServer":
                return !(node.path("error").isMissingNode());

            case "ObstacleListToServer":
                return !(node.path("obstacles").isMissingNode());

            case "PathRequestToServer":
                return true;

            case "SpeedToServer":

                return !(node.path("speed").isMissingNode());

            case "StatusToServer":
                return !(node.path("status").isMissingNode());

            case "":
            default:
                return false;
        }
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
