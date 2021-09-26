#!/usr/bin/env bash
################################################################################
#
# Start/stop a daemon
#
# Usage: appctl.sh (start|stop|stop-all) (app) [args]
#
################################################################################

set -e

#################################################################################
USAGE="Usage: appctl.sh (start|stop|stop-all) [app] (port) (args)"

COMMAND=${1}
APP=${2}
PORT=${3}
ARGS=("${@:4}") # get remaining arguments as array

# Work directory
bin=`dirname "${0}"`
bin=`cd "${bin}"; pwd`

# Load configuration
. "${bin}"/config.sh

# Generate tmp file which save active pid information
mkdir -p "${PID_DIR}"

if [ "${PID_FILE_IDENT_PREFIX}" = "" ]; then
    PID_FILE_IDENT_PREFIX="${USER}"
fi

pid="${PID_DIR}/${PID_FILE_IDENT_PREFIX}-${APP}.pid"

# Ascending ID depending on number of lines in pid file.
# This allows us to start multiple daemon of each type.
id=$([ -f "${pid}" ] && echo $(wc -l < "${pid}") || echo "0")

if [ -z "${APP_NAME}" ]; then
    export APP_NAME=${APP}
fi
if [ -z "${APP_PORT}" ]; then
    export APP_PORT=${PORT}
fi
if [ -z "${APP_INSTANCE_ID}" ]; then
    export APP_INSTANCE_ID="${APP}-${id}-${HOSTNAME}"
fi

LOG_PREFIX="${LOG_DIR}/${APP_INSTANCE_ID}"
log="${LOG_PREFIX}.log"
out="${LOG_PREFIX}.out"

if [ -z "${LOG_CONFIG}" ]; then
    export LOG_CONFIG="${DEFAULT_ENV_LOG_CONFIG}"
fi
if [ -z "${LOG_FILE}" ]; then
    export LOG_FILE="${log}"
fi

# log settings
log_setting=()

# app settings
app_setting=()

if [ "${SW_ENABLED}" = true ]; then
    JAVA_OPTS+=" -javaagent:${SW_AGENT_PATH}"
    app_setting+=("-Dskywalking.agent.service_name=${APP_NAME}")
    app_setting+=("-Dskywalking.agent.instance_name=${APP_INSTANCE_ID}")
fi

if [ "${APP_PORT}" ]; then
    app_setting+=("-Dserver.port=${APP_PORT}")
fi

JAR_TO_RUN="${APP_DIR}/${APP_NAME}-*.jar"

# Auxiliary functions for log file rotation
rotateLogFilesWithPrefix() {
    dir=$1
    prefix=$2
    while read -r log ; do
        rotateLogFile "$log"
        # find distinct set of log file names, ignoring the rotation number (trailing dot and digit)
    done < <(find "$dir" ! -type d -path "${prefix}*" | sed -E s/\.[0-9]+$// | sort | uniq)
}

rotateLogFile() {
    log=$1;
    num=$MAX_LOG_FILE_NUMBER
    if [ -f "$log" -a "$num" -gt 0 ]; then
        while [ $num -gt 1 ]; do
            prev=`expr $num - 1`
            [ -f "$log.$prev" ] && mv "$log.$prev" "$log.$num"
            num=$prev
        done
        mv "$log" "$log.$num";
    fi
}

case "${COMMAND}" in

    (start)
    mkdir -p "${LOG_DIR}"

    # Rotate log files
    rotateLogFilesWithPrefix "${LOG_DIR}" "${LOG_PREFIX}"

    # Print a warning if daemons are already running on host
    if [ -f "${pid}" ]; then
        active=()
        while IFS='' read -r p || [[ -n "$p" ]]; do
            kill -0 $p >/dev/null 2>&1
            if [ $? -eq 0 ]; then
                active+=($p)
            fi
        done < "${pid}"

        count="${#active[@]}"

        if [ ${count} -gt 0 ]; then
            echo "[INFO] ${count} instance(s) of ${APP_NAME} are already running on ${HOSTNAME}."
        fi
    fi

    # Evaluate user options for local variable expansion
    JAVA_OPTS=$(eval echo ${JAVA_OPTS})

    echo "Starting ${APP_INSTANCE_ID} daemon on host ${HOSTNAME}"
    nohup $JAVA_RUN ${JVM_ARGS} ${JAVA_OPTS} "${log_setting[@]}" "${app_setting[@]}" -jar ${JAR_TO_RUN} "${ARGS[@]}" > "${out}" 200<&- 2>&1 < /dev/null &

    # Get last process id from shell
    mypid=$!

    sleep 5
    # Add to pid file if successful start
    if [[ ${mypid} =~ ${IS_NUMBER} ]] && kill -0 ${mypid} > /dev/null 2>&1 ; then
        echo ${mypid} >> "${pid}"
    else
        echo "Error starting ${APP_NAME} daemon."
        exit 1
    fi
;;

(stop)
if [ -f "$pid" ]; then
    # Remove last in pid file
    to_stop=$(tail -n 1 "$pid")

    if [ -z $to_stop ]; then
        rm "$pid" # If all stopped, clean up pid file
        echo "No $APP_NAME daemon to stop on host $HOSTNAME."
    else
        sed \$d "$pid" > "$pid.tmp" # all but last line

        # If all stopped, clean up pid file
        [ $(wc -l < "$pid.tmp") -eq 0 ] && rm "$pid" "$pid.tmp" || mv "$pid.tmp" "$pid"

        if kill -0 $to_stop > /dev/null 2>&1; then
            echo "Stopping $APP_NAME daemon (pid: $to_stop) on host $HOSTNAME."
            kill $to_stop
        else
            echo "No $APP_NAME daemon (pid: $to_stop) is running anymore on $HOSTNAME."
        fi
    fi
else
    echo "No $APP_NAME daemon to stop on host $HOSTNAME."
fi
;;

(stop-all)
if [ -f "$pid" ]; then
mv "$pid" "${pid}.tmp"

while read to_stop; do
    if kill -0 $to_stop > /dev/null 2>&1; then
        echo "Stopping $APP_NAME daemon (pid: $to_stop) on host $HOSTNAME."
        kill $to_stop
    else
        echo "Skipping $APP_NAME daemon (pid: $to_stop), because it is not running anymore on $HOSTNAME."
    fi
done < "${pid}.tmp"
rm "${pid}.tmp"
fi
;;

(*)
echo "Unexpected argument '$COMMAND'. $USAGE."
exit 1
;;

esac
