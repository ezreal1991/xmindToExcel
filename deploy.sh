#!/bin/bash -l
DONTKILLME
export PROJ_PATH=`pwd`
export TOMCAT_PATH=/usr/local/tomcat/apache-tomcat-9.0.14
cd $PROJ_PATH/
jar -cvf ROOT.war ./*
cp $PROJ_PATH/ROOT.war $TOMCAT_PATH/webapps/
cd $TOMCAT_PATH/webapps/