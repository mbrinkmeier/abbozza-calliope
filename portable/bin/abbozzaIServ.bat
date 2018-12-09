@echo off

set PATH=%cd%\tools\jre\bin;%PATH%
set ISERVHOME=...

start /B javaw -Duser.home=%ISERVHOME% -cp lib\jssc-2.8.0.jar;lib\rsyntaxtextarea.jar;lib\autocomplete.jar;lib\abbozza-calliope.jar;lib\commons-io-2.5.jar de.uos.inf.did.abbozza.calliope.AbbozzaCalliopeC
