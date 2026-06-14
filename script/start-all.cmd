@echo off
rem aldemohk backend + frontend launcher (Windows)
rem Close this window to stop both processes
setlocal

set "PROJECT_CODE=aldemohk"

echo ==================================
echo   %PROJECT_CODE% Full Stack Launcher
echo   Close window to stop all
echo ==================================

start "%PROJECT_CODE%-backend" cmd /c "%~dp0start-backend.cmd"
start "%PROJECT_CODE%-frontend" cmd /c "%~dp0start-frontend.cmd"

echo Both services started in separate windows.
echo Close those windows to stop.
pause
