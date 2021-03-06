#!/bin/bash

#RUN_AI="python python/game.py"

# for Java, uncomment this line:
RUN_AI="java -jar target/awap-spacerace-snapshot.jar"

# for CPP, uncomment this line:
# RUN_AI="./cpp/game"

TEAM_ID="test"
FAST=0

trap "pkill -P $$" SIGTERM SIGKILL EXIT

while getopts "ft:" option;
do
    case $option in
        f) FAST=1 ;;
        t) TEAM_ID=$OPTARG ;;
    esac
done

python client.py "$RUN_AI" $TEAM_ID $FAST &

wait