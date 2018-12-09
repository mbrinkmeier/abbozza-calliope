@echo off

REM abbozza! build script

call portableSetPaths.bat

cd %ABBOZZAHOME%\build\calliope

echo Building in %CD% ...

yt -n -v build

echo ... done

pause
