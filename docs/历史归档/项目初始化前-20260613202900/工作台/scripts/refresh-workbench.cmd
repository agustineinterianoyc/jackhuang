@echo off
setlocal
cd /d "%~dp0\..\..\.."
node docs\工作台\scripts\build-workbench-data.js
echo.
echo Workbench data refreshed.
