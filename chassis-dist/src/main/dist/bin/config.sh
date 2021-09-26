#!/usr/bin/env bash
set -e

########################################################################################################################
# PATHS AND CONFIG
########################################################################################################################

target="$0"
# For the case, the executable has been directly symlinked, figure out
# the correct bin path by following its symlink up to an upper bound.
# Note: we can't use the readlink utility here if we want to be POSIX
# compatible.
iteration=0
while [ -L "$target" ]; do
    if [ "$iteration" -gt 100 ]; then
        echo "Cannot resolve path: You have a cyclic symlink in $target."
        break
    fi
    ls=`ls -ld -- "$target"`
    target=`expr "$ls" : '.* -> \(.*\)$'`
    iteration=$((iteration + 1))
done

# Work directory
CUR_DIR=`dirname "${0}"`
SYMLINK_RESOLVED_BIN=`cd "${CUR_DIR}"; pwd -P`
WORK_HOME=`dirname "$SYMLINK_RESOLVED_BIN"`
BIN_DIR=$WORK_HOME/bin
APP_DIR=$WORK_HOME/app
CONF_DIR=$WORK_HOME/config
LOG_DIR=$WORK_HOME/log

SERVICE_FILE=${CONF_DIR}/service
ENV_FILE=${CONF_DIR}/.env

IS_NUMBER="^[0-9]+$"

########################################################################################################################
# ENVIRONMENT VARIABLES
########################################################################################################################

# Local .env
if [ -f ${ENV_FILE} ]; then
    # Load Environment Variables
    export $(cat ${ENV_FILE} | grep -v '#' | awk '/=/ {print $1}')
fi

# Define HOSTNAME if it is not already set
if [ -z "${HOSTNAME}" ]; then
    HOSTNAME=`hostname`
fi

# check if we have a valid JAVA_HOME and if java is not available
if [ -z "${MY_JAVA_HOME}" ]; then
    # config did not specify JAVA_HOME. Use system JAVA_HOME
    MY_JAVA_HOME=${JAVA_HOME}
fi

if [ -z "${MY_JAVA_HOME}" ] && ! type java > /dev/null 2> /dev/null; then
    echo "Please specify JAVA_HOME. Either in config ./conf/conf.yaml or as system-wide JAVA_HOME."
    exit 1
else
    JAVA_HOME=${MY_JAVA_HOME}
fi

UNAME=$(uname -s)
if [ "${UNAME:0:6}" == "CYGWIN" ]; then
    JAVA_RUN=java
else
    if [[ -d $JAVA_HOME ]]; then
        JAVA_RUN=$JAVA_HOME/bin/java
    else
        JAVA_RUN=java
    fi
fi

# Arguments for the JVM. Used for JVMs.
if [ -z "${JVM_ARGS}" ]; then
    JVM_ARGS=""
fi

if [ -z "${JAVA_OPTS}" ]; then
    # Remove leading and ending double quotes (if present) of value
    JAVA_OPTS="$( echo "${JAVA_OPTS}" | sed -e 's/^"//'  -e 's/"$//' )"
fi