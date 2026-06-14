@echo off
rem aldemohk frontend launcher (Windows)
setlocal

set "PROJECT_CODE=aldemohk"

echo ==================================
echo   %PROJECT_CODE% Frontend Launcher
echo ==================================

cd /d "%~dp0..\code\frontend"
call npm run dev
