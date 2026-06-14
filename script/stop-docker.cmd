@echo off
rem aldemohk docker stopper (Windows)
setlocal

set "SCRIPT_DIR=%~dp0"
set "ROOT_DIR=%SCRIPT_DIR%.."
set "COMPOSE_FILE=%ROOT_DIR%\docker\docker-compose.yml"

echo ========================================
echo  Stopping aldemohk containers ...
echo ========================================

pushd "%ROOT_DIR%" || exit /b 1
docker compose -f "%COMPOSE_FILE%" down
set "EXITCODE=%ERRORLEVEL%"
popd
exit /b %EXITCODE%
