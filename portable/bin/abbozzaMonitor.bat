@echo off

set CP=..\lib\jSerialComm-2.4.2.jar;..\lib\rsyntaxtextarea.jar
set CP=%CP%;..\lib\autocomplete.jar;..\lib\abbozza-calliope.jar
set CP=%CP%;..\lib\commons-io-2.5.jar;..\lib\websockets.jar;..\lib\slf4j-api-1.7.25.jar

javaw -cp %CP% de.uos.inf.did.abbozza.monitor.AbbozzaMonitorServer
