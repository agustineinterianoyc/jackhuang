@echo off
rem aldemo backend launcher (Windows)
setlocal

set "PROJECT_NAME=GongSiPeiXunXiangMu"
set "PROJECT_CODE=aldemo"
set "BACKEND_MODULE=aldemo"
set "SERVER_PORT_DEFAULT=8080"

set "SCRIPT_DIR=%~dp0"
set "BACKEND_DIR=%SCRIPT_DIR%..\code\backend"

echo ========================================
echo  Project   : %PROJECT_NAME% (%PROJECT_CODE%)
echo  Module    : %BACKEND_MODULE%
echo  Port      : %SERVER_PORT_DEFAULT%
echo  Workdir   : %BACKEND_DIR%
echo ========================================

pushd "%BACKEND_DIR%" || exit /b 1
call mvnw.cmd -pl %BACKEND_MODULE% -am spring-boot:run %*
set "EXITCODE=%ERRORLEVEL%"
popd
exit /b %EXITCODE%
