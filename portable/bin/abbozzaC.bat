@echo off

set ENV_PATH=%CD%\..\tools
set PATH=%ENV_PATH%\bin;%ENV_PATH%\arm-none-eabi\bin;%PATH%;%ENV_PATH%\jre\bin

java -cp ..\lib\jssc-2.8.0.jar;..\lib\rsyntaxtextarea.jar;..\lib\autocomplete.jar;..\lib\abbozza-calliope.jar;..\lib\commons-io-2.5.jar de.uos.inf.did.abbozza.calliope.AbbozzaCalliopeC

