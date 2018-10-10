#!/usr/bin/env bash

if [ $# -ne 4 ]; then
  echo "usage: startNode.sh <ipc_dir> <log_dir> <node_id> <test_id>"
  exit 1
fi

. ./readconfig

ipc_dir=$1
log_dir=$2
node_id=$3
test_id=$4

ismine="mine"

/home/ardhipoetra/git/eth/gik/src/github.com/ethereum/go-ethereum/_scripts/source_export
#gvm use go1.9.3

if [ $node_id -gt 0 ]; then
   ismine="nomine"
fi

pid=`/home/ardhipoetra/git/eth/gik/src/github.com/ethereum/go-ethereum/_scripts/run_node.sh $node_id $log_dir $ismine $ipc_dir`
echo $node_id":"$pid >> pid_file
