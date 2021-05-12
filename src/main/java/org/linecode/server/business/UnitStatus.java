/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source licence (see accompanying file LICENCE).
 */
package org.linecode.server.business;

public enum UnitStatus {
    GOINGTO(0),STOP(1),BASE(2),ERROR(3),DISCONNECTED(4);

    private Integer code;
    UnitStatus(final Integer code) {
        this.code=code;
    }
    public Integer getCode(){
        return code;
    }
}
