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

source /home/ardhipoetra/git/eth/gik/go-ethereum/_scripts/source_export
gvm use go1.9.3

/home/ardhipoetra/git/eth/gik/go-ethereum/_scripts/run_node.sh $node_id nomine $log_dir&
echo $nodeId":"$! >> pid_file
