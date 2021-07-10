/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source licence (see accompanying file LICENCE).
 */

package org.linecode.server;

import org.glassfish.tyrus.server.Server;
import org.linecode.server.api.UiEndpoint;
import org.linecode.server.api.UnitEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import javax.websocket.DeploymentException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Set;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        if (seed()) {
            logger.info("Empty database seeded");
        } else {
            logger.info("Database already seeded");
        }

        Server server = new Server(
                "0.0.0.0",
                8080,
                "/",
                null,
                Set.of(UiEndpoint.class, UnitEndpoint.class));

        try {
            server.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            logger.info("Grizzly HTTP server is up. Please press a key to stop the server.");
            reader.readLine();
        } catch (Exception e) {
            logger.error("Grizzly HTTP server error", e);
            e.printStackTrace();
        } finally {
            System.out.println("Stopping Grizzly HTTP server");
            server.stop();
        }
    }

    private static boolean seed() {
        Jedis db = new Jedis();

        if (db.exists("length") && db.exists("height")) {
            return false;
        }

        db.flushDB();
        db.sadd("user", "valton");
        db.hmset("valton", Map.of("password", "tahiraj", "admin", "true"));

        db.set("length", "6");
        db.set("height", "4");

        db.hmset("cell:0:0", Map.of("locked", "false", "poi", "false", "base", "true", "direction", "ALL"));
        db.hmset("cell:1:0", Map.of("locked", "false", "poi", "false", "base", "false", "direction", "ALL"));
        db.hmset("cell:2:0", Map.of("locked", "false", "poi", "false", "base", "false", "direction", "LEFT"));
        db.hmset("cell:3:0", Map.of("locked", "false", "poi", "false", "base", "false", "direction", "ALL"));
        db.hmset("cell:4:0", Map.of("locked", "false", "poi", "false", "base", "false", "direction", "ALL"));
        db.hmset("cell:5:0", Map.of("locked", "false", "poi", "true", "base", "false", "direction", "ALL"));

        db.hmset("cell:0:1", Map.of("locked", "false", "poi", "false", "base", "false", "direction", "ALL"));
        db.hmset("cell:1:1", Map.of("locked", "false", "poi", "false", "base", "false", "direction", "ALL"));
        db.hmset("cell:2:1", Map.of("locked", "false", "poi", "false", "base", "false", "direction", "RIGHT"));
        db.hmset("cell:3:1", Map.of("locked", "false", "poi", "false", "base", "false", "direction", "ALL"));
        db.hmset("cell:4:1", Map.of("locked", "true", "poi", "false", "base", "false", "direction", "ALL"));
        db.hmset("cell:5:1", Map.of("locked", "false", "poi", "false", "base", "false", "direction", "ALL"));

        db.hmset("cell:0:2", Map.of("locked", "false", "poi", "false", "base", "true", "direction", "ALL"));
        db.hmset("cell:1:2", Map.of("locked", "false", "poi", "false", "base", "false", "direction", "ALL"));
        db.hmset("cell:2:2", Map.of("locked", "false", "poi", "false", "base", "false", "direction", "RIGHT"));
        db.hmset("cell:3:2", Map.of("locked", "false", "poi", "false", "base", "false", "direction", "ALL"));
        db.hmset("cell:4:2", Map.of("locked", "true", "poi", "true", "base", "false", "direction", "ALL"));
        db.hmset("cell:5:2", Map.of("locked", "false", "poi", "false", "base", "false", "direction", "ALL"));

        db.hmset("cell:0:3", Map.of("locked", "false", "poi", "false", "base", "true", "direction", "ALL"));
        db.hmset("cell:1:3", Map.of("locked", "false", "poi", "false", "base", "false", "direction", "ALL"));
        db.hmset("cell:2:3", Map.of("locked", "false", "poi", "false", "base", "false", "direction", "LEFT"));
        db.hmset("cell:3:3", Map.of("locked", "false", "poi", "false", "base", "false", "direction", "ALL"));
        db.hmset("cell:4:3", Map.of("locked", "false", "poi", "false", "base", "false", "direction", "ALL"));
        db.hmset("cell:5:3", Map.of("locked", "false", "poi", "true", "base", "false", "direction", "ALL"));

        db.save();
        db.disconnect();
        return true;
    }
}
