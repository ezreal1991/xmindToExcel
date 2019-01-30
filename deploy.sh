#!/bin/bash -l
BUILD_ID=DONTKILLME
. /etc/profile
export PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/root/bin
export PROJ_PATH=`pwd`
export TOMCAT_PATH=/usr/local/tomcat/apache-tomcat-9.0.14
jar -cvf ROOT.war ./*
cp $PROJ_PATH/ROOT.war $TOMCAT_PATH/webapps/