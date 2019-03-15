@echo off

set ENV_PATH=%CD%\..\tools
set PATH=%ENV_PATH%\bin;%ENV_PATH%\arm-none-eabi\bin;%PATH%;%ENV_PATH%\jre\bin
set CP=..\lib\jSerialComm-2.4.2.jar;..\lib\rsyntaxtextarea.jar
set CP=%CP%;..\lib\autocomplete.jar;..\lib\abbozza-calliope.jar
set CP=%CP%;..\lib\commons-io-2.5.jar;..\lib\websockets.jar;..\lib\slf4j-api-1.7.25.jar

java -cp %CP% de.uos.inf.did.abbozza.calliope.AbbozzaCalliopeC

