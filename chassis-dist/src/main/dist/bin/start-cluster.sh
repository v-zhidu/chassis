#!/usr/bin/env bash
set -e

bin=`dirname "$0"`
bin=`cd "$bin"; pwd`

. "$bin"/config.sh

if [ -f "${SERVICE_FILE}" ]; then
    echo "Starting cluster."
    while IFS='' read -r p || [[ -n "$p" ]]; do
        APP_NAME="$(cut -d':' -f1 <<<"$p")"
        APP_PORT="$(cut -d':' -f2 <<<"$p")"
        if [ "${APP_NAME}" ]; then
            "${BIN_DIR}"/appctl.sh start ${APP_NAME} ${APP_PORT}
        fi
    done < "${SERVICE_FILE}"
    echo "Cluster started."
else
    echo "Not found ${SERVICE_FILE}"
fi
