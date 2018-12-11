@echo off

set ENV_PATH=%CD%\..\tools
set PATH=%ENV_PATH%\bin;%ENV_PATH%\arm-none-eabi\bin;%PATH%;%ENV_PATH%\jre\bin

javaw -cp ..\lib\jssc-2.8.0.jar;..\lib\abbozza-calliope.jar de.uos.inf.did.abbozza.monitor.AbbozzaMonitorServer
