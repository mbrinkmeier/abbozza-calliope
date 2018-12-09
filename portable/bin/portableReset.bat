@echo off

REM abbozza! build cleanup script
REM
REM This script cleans the abbozza! portable build system

call portableSetPaths.bat

echo Copying default configuration to %ABBOZZAHOME% ...
copy ..\lib\calliopeC.cfg %ABBOZZAHOME%\abbozza.cfg
echo ... done

pause
