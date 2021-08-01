#!/bin/sh

#
# PORTACS
# piattaforma di controllo mobilità autonoma
#
# Copyright (c) lineCode group <linecode.swe@gmail.com> 2020 - 2021
# Distributed under ISC license (see accompanying file LICENSE).
################################################################################

redis-server /etc/redis.conf --appendonly yes --loglevel verbose --daemonize yes;
java -jar start.jar;
