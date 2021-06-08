/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source licence (see accompanying file LICENCE).
 */
package org.linecode.server.api.message;

public class CommandToUnit extends Message{
    public final UnitStopCommand command;

    public CommandToUnit(UnitStopCommand command) {
        super("CommandToUnit");
        this.command = command;
    }

    public UnitStopCommand getCommand() {
        return command;
    }
}
