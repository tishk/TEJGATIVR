#!/bin/bash
# Grabs and kill a process from the pidlist that has the word myapp


# chkconfig: 345 99 01
# description: some startup script

pid=`ps aux | grep Telbank | awk '{print $2}'`
kill -9 $pid