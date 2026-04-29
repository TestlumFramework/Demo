@echo off
set MODE=%~1

if "%MODE%"=="success" goto :success
if "%MODE%"=="error" goto :error
if "%MODE%"=="slow" goto :slow
if "%MODE%"=="env" goto :env

echo Usage: %~nx0 {success^|error^|slow^|env^|math}
exit /b 3

:success
echo Standard output message
echo Line 2 of output
exit /b 0

:error
echo This is a normal message
echo CRITICAL: This is an error message
exit /b 1

:slow
echo Starting slow process...
timeout /t 2 /nobreak > NUL
echo Process complete
exit /b 0

:env
if "%TEST_VAR%"=="" (
    echo TEST_VAR_VALUE=NOT_SET
) else (
    echo TEST_VAR_VALUE=%TEST_VAR%
)
exit /b 0