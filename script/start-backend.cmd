@echo off
rem aldemohk backend launcher (Windows)
setlocal

set "PROJECT_CODE=aldemohk"
set "BACKEND_MODULE=aldemohk"

echo ==================================
echo   %PROJECT_CODE% Backend Launcher
echo   Module: %BACKEND_MODULE%
echo   Port: 8080
echo ==================================

cd /d "%~dp0..\code\backend"
call mvnw -pl %BACKEND_MODULE% -am spring-boot:run
