/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright (c) lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under ISC license (see accompanying file LICENSE).
 ******************************************************************************/
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
