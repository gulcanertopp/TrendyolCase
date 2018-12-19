#!/bin/bash

################################################################################
#  Licensed to the Apache Software Foundation (ASF) under one
#  or more contributor license agreements.  See the NOTICE file
#  distributed with this work for additional information
#  regarding copyright ownership.  The ASF licenses this file
#  to you under the Apache License, Version 2.0 (the
#  "License"); you may not use this file except in compliance
#  with the License.  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
# limitations under the License.
################################################################################

JOBMANAGER_HOSTNAME="$2"

function read_env {
  for var in `env`
  do
    if [[ "$var" =~ ^flink_ ]]; then
      env_var=`echo "$var" | sed -r "s/(.*)=.*/\1/g"`
      flink_property=`echo "$env_var" | tr _ .  | sed -e "s/flink.//g"`

      if grep -q "$flink_property:" $FLINK_HOME/conf/flink-conf.yaml; then
        sed -i '/'"$flink_property"'/c\'"$flink_property"': '"${!env_var}" $FLINK_HOME/conf/flink-conf.yaml
      else
        echo "$flink_property: ${!env_var}" >> $FLINK_HOME/conf/flink-conf.yaml
      fi
    fi
  done
}

if [ -z "$JOBMANAGER_HOSTNAME" ]; then
    # make use of Docker container linking and exploit the jobmanager entry in /etc/hosts
    JOBMANAGER_HOSTNAME="jobmanager"
fi

if [ "$1" = "jobmanager" ]; then
    export flink_jobmanager_rpc_address=$JOBMANAGER_HOSTNAME
    read_env

    echo "Starting JobManager"
    echo "config file: " && grep '^[^\n#]' $FLINK_HOME/conf/flink-conf.yaml
    $FLINK_HOME/bin/jobmanager.sh start cluster
    echo "Sleeping 3 seconds, then start to tail the log file"
    sleep 3 && tail -c +1 -f `ls $FLINK_HOME/log/*.log | head -n1`
elif [ "$1" = "taskmanager" ]; then

    export flink_jobmanager_rpc_address=$JOBMANAGER_HOSTNAME
    read_env

    echo "Starting TaskManager"
    echo "config file: " && grep '^[^\n#]' $FLINK_HOME/conf/flink-conf.yaml
    $FLINK_HOME/bin/taskmanager.sh start
    echo "Sleeping 3 seconds, then start to tail the log file"
    sleep 3 && tail -c +1 -f `ls $FLINK_HOME/log/*.log | head -n1`
elif [ "$1" = "historyserver" ]; then

    export flink_jobmanager_rpc_address=$JOBMANAGER_HOSTNAME
    read_env

    echo "Starting History Server"
    echo "config file: " && grep '^[^\n#]' $FLINK_HOME/conf/flink-conf.yaml
    $FLINK_HOME/bin/historyserver.sh start
    echo "Sleeping 3 seconds, then start to tail the log file"
    sleep 3 && tail -c +1 -f `ls $FLINK_HOME/log/*.log | head -n1`
else
    $@
fi
