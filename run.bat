@echo off
start "" java -jar backend-0.0.1-SNAPSHOT.jar -Dspring.config.location=application.properties
ping 127.0.0.1 -n 15 > NUL && start "" http://127.0.0.1:5000
