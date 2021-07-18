/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source licence (see accompanying file LICENCE).
 */

package org.linecode.server.api.message;

public class UnitStopFromUi extends Message {
    private final String id;
    private final UnitStopCommand command;

    public UnitStopFromUi(String id, UnitStopCommand command) {
        super("UnitStopFromUi");
        this.id = id;
        this.command = command;
    }

    public String getId() {
        return id;
    }

    public UnitStopCommand getCommand() {
        return command;
    }
}
