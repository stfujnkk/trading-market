@echo off

start "nginx-1.22.0" /min cmd /c "cd /D D:\env\nginx-1.22.0&&.\nginx.exe"
start "nacos 2.1.0" /min cmd /c "D:\env\nacos\bin\startup.cmd -m standalone"
start "redis-x64-3.2.100" /min cmd /c "D:\env\fingard\tools\redis-x64-3.2.100\redis-server.exe"
start "elasticsearch-7.4.2" /min cmd /c "CHCP 65001&&D:\env\elasticsearch-7.4.2\bin\elasticsearch.bat"
start "kibana-7.4.2" /min cmd /c "D:\env\kibana-7.4.2-windows-x86_64\bin\kibana.bat"
