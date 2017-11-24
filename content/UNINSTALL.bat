:: Kill java process
taskkill /f /im javaw.exe

:: Remove (s)AINT folder
rmdir /s /q %appdata%\(s)AINT

:: Delete entry registry
reg delete HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Run /v Security /f

pause
