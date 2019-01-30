#!/bin/bash -l
BUILD_ID=DONTKILLME
. /etc/profile
export PATH=$PATH:$HOME/bin:/sbin:/usr/bin:/usr/sbin
export PROJ_PATH=`pwd`
export TOMCAT_PATH=/usr/local/tomcat/apache-tomcat-9.0.14
jar -cvf ROOT.war ./*
cp $PROJ_PATH/ROOT.war $TOMCAT_PATH/webapps/