@echo off

REM This script sets all paths for the portable scripts

REM Set the home path for the user
set USERHOME=%USERPROFILE%
REM set USERHOME=H:

REM set the path to the users abbozza directory
set ABBOZZAHOME=%USERHOME%\.abbozza\calliopeC

REM Set the tool path
set ABBOZZA=%CD%\..
set TOOLPATH=%ABBOZZA%\tools\bin;%ABBOZZA%\tools\arm-none-eabi\bin;%ABBOZZA%\tools\Python;%ABBOZZA%\tools\Python\python-3.6.2;%ABBOZZA%\tools\Python\python-3.6.2\Scripts;%ABBOZZA%\tools\jre\bin

REM Add the toolpath to the system path
set PATH=%TOOLPATH%;%PATH%

echo Users abbozza directory is %ABBOZZAHOME%
echo abbozza! Portable found in %CD%\..
