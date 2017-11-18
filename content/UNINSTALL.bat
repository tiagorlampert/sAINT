:: Kill java process
taskkill /f /im javaw.exe

:: Delete (s)AINT folder
rmdir /s /q %appdata%\(s)AINT

:: Delete entry registry
reg delete HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Run /v Security /f

pause
