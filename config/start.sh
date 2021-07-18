#!/bin/sh

redis-server /etc/redis.conf --appendonly yes --loglevel verbose --daemonize yes;
java -jar start.jar;
