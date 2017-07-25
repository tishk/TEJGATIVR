#!/bin/bash
# Grabs and kill a process from the pidlist that has the word Telbank


# chkconfig: 345 99 01
# description: some startup script


pkill java

sleep 2s


pkill org.asteriskjava.fastagi.DefaultAgiServer

sleep 2s