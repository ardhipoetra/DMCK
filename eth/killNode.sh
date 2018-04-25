#!/usr/bin/env bash

if [ $# -ne 1 ]; then
  echo "usage: killNode.sh <nodeId>"
  exit 1
fi

nodeId=$1

while read line
do
	arrLine=(${line//:/ })
	if [ ${arrLine[0]} -eq $nodeId ]; then
		kill -SIGINT ${arrLine[1]}
		sed -i "/$line/d" pid_file
	fi
done < pid_file
