/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source licence (see accompanying file LICENCE).
 */

package org.linecode.server.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import org.linecode.server.api.message.KeepAliveToUiEncoder;
import org.linecode.server.api.message.MessageDecoder;
import org.linecode.server.business.MapService;
import org.linecode.server.business.MapServiceImpl;
import org.linecode.server.business.UnitService;
import org.linecode.server.business.UnitServiceImpl;
import org.linecode.server.business.UserService;
import org.linecode.server.business.UserServiceImpl;
import org.linecode.server.persistence.MapRepository;
import org.linecode.server.persistence.MapRepositoryRedis;
import org.linecode.server.persistence.ObstacleRepository;
import org.linecode.server.persistence.ObstacleRepositoryRedis;
import org.linecode.server.persistence.UnitRepository;
import org.linecode.server.persistence.UnitRepositoryRedis;
import org.linecode.server.persistence.UserRepository;
import org.linecode.server.persistence.UserRepositoryRedis;

public class EndpointModule extends AbstractModule {
    protected void configure() {
        bind(UserRepository.class).to(UserRepositoryRedis.class).asEagerSingleton();
        bind(UnitRepository.class).to(UnitRepositoryRedis.class).asEagerSingleton();
        bind(ObstacleRepository.class).to(ObstacleRepositoryRedis.class).asEagerSingleton();
        bind(MapRepository.class).to(MapRepositoryRedis.class).asEagerSingleton();

        bind(UserService.class).to(UserServiceImpl.class).asEagerSingleton();
        bind(UnitService.class).to(UnitServiceImpl.class).asEagerSingleton();
        bind(MapService.class).to(MapServiceImpl.class).asEagerSingleton();

        bind(ResetTimer.class).to(ResetTimerImpl.class);
        bind(ObjectMapper.class).asEagerSingleton();

        requestStaticInjection(MessageDecoder.class);
        requestStaticInjection(KeepAliveToUiEncoder.class);
    }
}
