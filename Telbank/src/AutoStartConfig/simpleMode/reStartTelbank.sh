#!/bin/sh
# Grabs and kill a process from the pidlist that has the word myapp


# chkconfig: 345 99 01
# description: restart telbank

#/ivr/stopTelbank.sh & sleep 5s & printf "\n" &/ivr/startTelbank.sh & printf "\n" 


#!/bin/bash

echo "Stop IVR :please wait...."


piddd=`ps aux | grep Telbank | awk '{print $2}'`

if ps -p $piddd> /dev/null
then
 kill -s SIGINT  $piddd
echo "Done stoping telbank" 
fi

pidd=`ps aux | grep org.asteriskjava.fastagi.DefaultAgiServer| awk '{print $2}'`


if ps -p $pidd> /dev/null
then
  kill -s SIGINT  $pidd
  echo "Done stoping IVR"
fi

sleep 10s

echo  "Restart Asterisk:Start"

amportal restart

echo "Restart Asterisk:Completed."

service hylafax restart

echo "Restart FaxService:Completed."

export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/ivr/SystemStatusLib

java -jar /ivr/Telbank.jar