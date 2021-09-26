#!/usr/bin/env bash
set -e

bin=`dirname "$0"`
bin=`cd "$bin"; pwd`

. "$bin"/config.sh

if [ -f "${SERVICE_FILE}" ]; then
    echo "Stoping cluster."
    while IFS='' read -r p || [[ -n "$p" ]]; do
        APP_NAME="$(cut -d':' -f1 <<<"$p")"
        if [ "${APP_NAME}" ]; then
            "${BIN_DIR}"/appctl.sh stop-all ${APP_NAME}
        fi
    done < "${SERVICE_FILE}"
    echo "Cluster stoped."
else
    echo "Not found ${SERVICE_FILE}"
fi
