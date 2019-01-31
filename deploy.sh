#!/bin/bash -ilex
BUILD_ID=DONTKILLME
. /etc/profile
export PROJ_PATH=`pwd`
export TOMCAT_PATH=/usr/local/tomcat/apache-tomcat-9.0.14
jar -cvf ROOT.war ./*
docker restart tomcat2