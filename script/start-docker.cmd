@echo off
rem aldemo docker launcher (Windows)
rem starts backend + frontend containers via docker compose
setlocal

set "PROJECT_NAME=GongSiPeiXunXiangMu"
set "PROJECT_CODE=aldemo"
set "SCRIPT_DIR=%~dp0"
set "ROOT_DIR=%SCRIPT_DIR%.."
set "COMPOSE_FILE=%ROOT_DIR%\docker\docker-compose.yml"
set "ENV_FILE=%ROOT_DIR%\docker\.env"

echo ========================================
echo  Project   : %PROJECT_NAME% (%PROJECT_CODE%)
echo  Compose   : %COMPOSE_FILE%
echo ========================================

if not exist "%ENV_FILE%" (
  echo [info] docker\.env not found, copying from .env.example ...
  copy /Y "%ROOT_DIR%\docker\.env.example" "%ENV_FILE%" >nul
)

pushd "%ROOT_DIR%" || exit /b 1
docker compose -f "%COMPOSE_FILE%" --env-file "%ENV_FILE%" up -d --build
set "EXITCODE=%ERRORLEVEL%"
if %EXITCODE% NEQ 0 goto :end

echo.
echo ========================================
echo  Backend  : http://localhost:8080
echo  Frontend : http://localhost:5173
echo  Health   : http://localhost:8080/actuator/health
echo ========================================
echo  Logs    : docker compose -f docker\docker-compose.yml logs -f backend
echo  Stop    : script\stop-docker.cmd
echo ========================================

:end
popd
exit /b %EXITCODE%
