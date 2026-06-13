@echo off
rem aldemo frontend launcher (Windows)
setlocal

set "PROJECT_NAME=GongSiPeiXunXiangMu"
set "PROJECT_CODE=aldemo"
set "FRONTEND_DEV_PORT_DEFAULT=5173"

set "SCRIPT_DIR=%~dp0"
set "FRONTEND_DIR=%SCRIPT_DIR%..\code\frontend"

echo ========================================
echo  Project   : %PROJECT_NAME% (%PROJECT_CODE%)
echo  Port      : %FRONTEND_DEV_PORT_DEFAULT%
echo  Workdir   : %FRONTEND_DIR%
echo ========================================

pushd "%FRONTEND_DIR%" || exit /b 1
if not exist node_modules (
  echo [info] node_modules not installed, running npm install ...
  call npm install
)
call npm run dev -- %*
set "EXITCODE=%ERRORLEVEL%"
popd
exit /b %EXITCODE%
