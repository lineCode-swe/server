/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright (c) lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under ISC license (see accompanying file LICENSE).
 ******************************************************************************/

package org.linecode.server.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import org.linecode.server.Main;
import org.linecode.server.api.message.AuthToUiEncoder;
import org.linecode.server.api.message.CommandToUnitEncoder;
import org.linecode.server.api.message.KeepAliveToUiEncoder;
import org.linecode.server.api.message.MapToUiEncoder;
import org.linecode.server.api.message.ObstaclesToUiEncoder;
import org.linecode.server.api.message.UiMessageDecoder;
import org.linecode.server.api.message.UnitErrorToUiEncoder;
import org.linecode.server.api.message.UnitPoiToUiEncoder;
import org.linecode.server.api.message.UnitPositionToUiEncoder;
import org.linecode.server.api.message.UnitSpeedToUiEncoder;
import org.linecode.server.api.message.UnitStatusToUiEncoder;
import org.linecode.server.api.message.UnitsToUiEncoder;
import org.linecode.server.api.message.UsersToUiEncoder;
import org.linecode.server.api.message.KeepAliveToUnitEncoder;
import org.linecode.server.api.message.UnitMessageDecoder;
import org.linecode.server.api.message.StartToUnitEncoder;
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
import redis.clients.jedis.Jedis;

public class EndpointModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Jedis.class).annotatedWith(Names.named("MapRepo")).toInstance(Main.jedisPool.getResource());
        bind(Jedis.class).annotatedWith(Names.named("UnitRepo")).toInstance(Main.jedisPool.getResource());
        bind(Jedis.class).annotatedWith(Names.named("ObstacleRepo")).toInstance(Main.jedisPool.getResource());
        bind(Jedis.class).annotatedWith(Names.named("UserRepo")).toInstance(Main.jedisPool.getResource());

        bind(UserRepository.class).to(UserRepositoryRedis.class).asEagerSingleton();
        bind(UnitRepository.class).to(UnitRepositoryRedis.class).asEagerSingleton();
        bind(ObstacleRepository.class).to(ObstacleRepositoryRedis.class).asEagerSingleton();
        bind(MapRepository.class).to(MapRepositoryRedis.class).asEagerSingleton();

        bind(UserService.class).to(UserServiceImpl.class).asEagerSingleton();
        bind(UnitService.class).to(UnitServiceImpl.class).asEagerSingleton();
        bind(MapService.class).to(MapServiceImpl.class).asEagerSingleton();

        bind(ObjectMapper.class).asEagerSingleton();

        requestStaticInjection(UnitMessageDecoder.class);
        requestStaticInjection(UiMessageDecoder.class);
        requestStaticInjection(KeepAliveToUiEncoder.class);
        requestStaticInjection(UnitsToUiEncoder.class);
        requestStaticInjection(UnitStatusToUiEncoder.class);
        requestStaticInjection(UnitPoiToUiEncoder.class);
        requestStaticInjection(UnitSpeedToUiEncoder.class);
        requestStaticInjection(UnitErrorToUiEncoder.class);
        requestStaticInjection(UnitPositionToUiEncoder.class);
        requestStaticInjection(UsersToUiEncoder.class);
        requestStaticInjection(MapToUiEncoder.class);
        requestStaticInjection(ObstaclesToUiEncoder.class);
        requestStaticInjection(AuthToUiEncoder.class);
        requestStaticInjection(KeepAliveToUnitEncoder.class);
        requestStaticInjection(StartToUnitEncoder.class);
        requestStaticInjection(CommandToUnitEncoder.class);
    }
}
