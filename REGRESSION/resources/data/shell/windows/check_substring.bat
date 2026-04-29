@echo off

echo Checking if '%~1' contains '%~2'...

echo %~1 | find "%~2" >nul

exit /b %ERRORLEVEL%