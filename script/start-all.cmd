@echo off
rem aldemo backend + frontend launcher (Windows)
rem opens two new windows for backend and frontend
setlocal

set "PROJECT_NAME=GongSiPeiXunXiangMu"
set "PROJECT_CODE=aldemo"
set "SCRIPT_DIR=%~dp0"

echo ========================================
echo  Project: %PROJECT_NAME% (%PROJECT_CODE%)
echo  Starting backend + frontend
echo  Stop : close each window
echo ========================================

start "aldemo-backend" cmd /k "%SCRIPT_DIR%start-backend.cmd"
start "aldemo-frontend" cmd /k "%SCRIPT_DIR%start-frontend.cmd"

endlocal
exit /b 0
