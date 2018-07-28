@echo off

set ENV_PATH=%CD%\..\tools
set PATH=%ENV_PATH%\bin;%ENV_PATH%\arm-none-eabi\bin;%ENV_PATH%\Python;%ENV_PATH%\Python\python-3.6.2;%ENV_PATH%\Python\python-3.6.2\Scripts;%ENV_PATH%\jre\bin;%PATH%

javaw -cp ..\lib\jssc-2.8.0.jar;..\lib\rsyntaxtextarea.jar;..\lib\autocomplete.jar;..\lib\abbozza-calliope.jar;..\lib\commons-io-2.5.jar de.uos.inf.did.abbozza.calliope.AbbozzaCalliopeC

