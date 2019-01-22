@echo off

set ENV_PATH=%CD%\..\tools
set PATH=%ENV_PATH%\bin;%ENV_PATH%\arm-none-eabi\bin;%PATH%

javaw -cp ..\lib\jssc-2.8.0.jar;..\lib\rsyntaxtextarea.jar;..\lib\autocomplete.jar;..\lib\abbozza-calliope.jar;..\lib\commons-io-2.5.jar;..\lib\websockets.jar:..\lib\slf4j-api-1.7.25.jar de.uos.inf.did.abbozza.calliope.AbbozzaCalliopeC

