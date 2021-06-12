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

import javax.inject.Inject;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.util.ArrayList;
import java.util.List;

public class UiMessageDecoder implements Decoder.Text<Message> {

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
                return new LoginFromUi(node.path("user").asText(), node.path("password").asText());

            case "LogoutToServer":
                return new LogoutFromUi();

            case "UserToServer":
                return new UserFromUi(
                        node.path("user").asText(),
                        node.path("password").asText(),
                        node.path("admin").asBoolean()
                );

            case "DeleteToServer":
                return new DeleteUserFromUi(node.path("user").asText());

            case "MapToServer":
                return new MapFromUi(node.path("mapConfig").asText());

            case "UnitStopToServer":
                return new UnitStopFromUi(
                        node.path("id").asText(),
                        UnitStopCommand.valueOf(node.path("command").asText())
                );

            case "UnitStartToServer":
                List<Position> poiList = new ArrayList<Position>();
                node.path("poiList").forEach(poi -> {
                    poiList.add(new Position(poi.path("x").asInt(), poi.path("y").asInt()));
                });
                return new UnitStartFromUi(node.path("id").asText(), poiList);

            case "UnitToServer":
                return new UnitFromUi(
                        node.path("id").asText(),
                        node.path("name").asText(),
                        new Position(
                                node.path("base").path("x").asInt(),
                                node.path("base").path("y").asInt()
                        )
                );

            case "DeleteUnitToServer":
                return new DeleteUnitFromUi(node.path("id").asText());

            case "DeleteUserToServer":
                return new DeleteUserFromUi(node.path("user").asText());

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
                return isLoginToServer(node);

            case "LogoutToServer":
                return true;

            case "DeleteToServer":
                return isDeleteToServer(node);

            case "UserToServer":
                return isUserToServer(node);

            case "MapToServer":
                return isMapToServer(node);

            case "UnitStopToServer":
                return isUnitStopToServer(node);

            case "UnitStartToServer":
                return isUnitStartToServer(node);

            case "UnitToServer":
                return isUnitToServer(node);

            case "DeleteUnitToServer":
                return isDeleteUnitToServer(node);

            case "DeleteUserToServer":
                return isDeleteUserToServer(node);

            case "":
            default:
                return false;
        }
    }

    private boolean isLoginToServer(JsonNode node) {
        return !(node.path("user").isMissingNode() ||
                node.path("password").isMissingNode());
    }

    private boolean isDeleteToServer(JsonNode node) {
        return !(node.path("user").isMissingNode());
    }

    private boolean isUserToServer(JsonNode node) {
        return !(node.path("user").isMissingNode() ||
                node.path("password").isMissingNode() ||
                node.path("admin").isMissingNode());
    }

    private boolean isMapToServer(JsonNode node) {
        return !(node.path("mapConfig").isMissingNode());
    }

    private boolean isUnitStopToServer(JsonNode node) {
        return !(node.path("id").isMissingNode() ||
                node.path("command").isMissingNode());
    }

    private boolean isUnitStartToServer(JsonNode node) {
        return !(node.path("id").isMissingNode() ||
                node.path("poiList").isMissingNode());
    }

    private boolean isUnitToServer(JsonNode node) {
        return !(node.path("id").isMissingNode() ||
                node.path("name").isMissingNode() ||
                node.path("base").isMissingNode());
    }

    private boolean isDeleteUnitToServer(JsonNode node) {
        return !(node.path("id").isMissingNode());
    }

    private boolean isDeleteUserToServer(JsonNode node) {
        return !(node.path("user").isMissingNode());
    }

    @Override
    public void init(EndpointConfig endpointConfig) {}

    @Override
    public void destroy() {}
}
