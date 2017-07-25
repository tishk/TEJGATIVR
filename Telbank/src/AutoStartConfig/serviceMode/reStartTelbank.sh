#!/bin/sh
# Grabs and kill a process from the pidlist that has the word myapp
# chkconfig: 345 99 01
# description: restart telbank
#!/bin/bash

echo "Stoping IVR :please wait...."
echo "    "
echo "------------Restarting Asterisk----------"
amportal restart
echo "    "
echo "------------Restarted Asterisk----------"
echo "    "
echo "------------Restarting Hylafax----------"
echo "    "

service hylafax restart

faxsetup

echo "    "
echo "------------Restarted Hylafax----------"

echo "------------Hylafax Log Cleaning----------"
echo "    "

rm -rf /var/spool/hylafax/doneq/*
echo "deleted doneq..."

rm -rf /var/spool/hylafax/recvq/*
echo "deleted recvq..."

rm -rf /var/spool/hylafax/log/*
echo "deleted log..."

rm -rf /var/spool/hylafax/sendq/*
echo "deleted sendq..."

rm -rf /var/spool/hylafax/docq/*
echo "deleted docq..."

rm -rf /var/spool/hylafax/pollq/*
echo "deleted pollq..."

echo "    "
echo "------------Hylafax Log Cleaned----------"


pkill java

sleep 2s


pkill org.asteriskjava.fastagi.DefaultAgiServer

sleep 2s

export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/ivr/SystemStatusLib

java -jar /ivr/Telbank.jar

