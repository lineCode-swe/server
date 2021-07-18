/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source licence (see accompanying file LICENCE).
 */

package org.linecode.server.api.message;

public class MapFromUi extends Message {
    private final String mapConfig;

    public MapFromUi(String mapConfig) {
        super("MapFromUi");
        this.mapConfig = mapConfig;
    }

    public String getMapConfig() {
        return mapConfig;
    }
}
