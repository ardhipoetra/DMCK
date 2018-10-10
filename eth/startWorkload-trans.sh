#!/usr/bin/env bash

# Read target-sys.conf
. ./readconfig

# Make sure that parameter is passed
if [ $# -ne 2 ]; then
  echo "Usage: startWorkload-trans.sh <test_id> <node_id>" > $working_dir/workload_error.log
  exit 1
fi

test_id=$1
node_id=$2


/home/ardhipoetra/git/eth/gik/src/github.com/ethereum/go-ethereum/_scripts/source_export
gvm use go1.9.3

# unlock account
out=`/home/ardhipoetra/git/eth/gik/src/github.com/ethereum/go-ethereum/_scripts/attach_command.sh $node_id 'personal.unlockAccount(eth.accounts[0], "")'`
echo $out >> $working_dir/dmck-workload.log

hash=`/home/ardhipoetra/git/eth/gik/src/github.com/ethereum/go-ethereum/_scripts/attach_command.sh $node_id 'eth.sendTransaction({from:eth.accounts[0], to:eth.accounts[0], value:1})'`
echo $hash >> $working_dir/dmck-workload.log

hash=`/home/ardhipoetra/git/eth/gik/src/github.com/ethereum/go-ethereum/_scripts/attach_command.sh $node_id 'eth.sendTransaction({from:eth.accounts[0], to:eth.accounts[0], value:1})'`
echo $hash >> $working_dir/dmck-workload.log

#echo $node_id:$! > $working_dir/dmck-workload.pid
