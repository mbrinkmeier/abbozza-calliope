@echo off

REM abbozza! build cleanup script
REM
REM This script cleans the abbozza! portable build system

call portableSetPaths.bat

cd %ABBOZZAHOME%\build\calliope

echo Cleaning abbozza! portable build system in %CD% ...

yt -n -v clean

echo ... done

pause
