#!/usr/bin/env bash
################################################################################
#
# Mirror YAML properties file to Helm Charts based on file name
#
# For example:
# ./file2helm copy folder/ --> config/{filename}/data

################################################################################
set -e

CUR_DIR=`dirname "${0}"`
SYMLINK_RESOLVED_BIN=`cd "${CUR_DIR}"; pwd -P`
WORK_HOME=`dirname "$SYMLINK_RESOLVED_BIN"`

#################################################################################
while getopts ":h" opt; do
  case ${opt} in
    h )
      echo "Usage:"
      echo "    file2helm -h                      Display this help message."
      echo "    file2helm copy <path>             copy <path>."
      exit 0
      ;;
   \? )
     echo "Invalid Option: -$OPTARG" 1>&2
     exit 1
     ;;
  esac
done
shift $((OPTIND -1))

subcommand=$1; shift # Remove `file2helm` from the argument list

case "$subcommand" in
  copy)
    targets=$1; shift # Remove `copy` from the argument list
    target_folder="./charts/charts"
    global_conf="conf/global"

    for f in $(find $target_folder -name 'chassis-*' -type d -d 1); do
        filename=$(basename -- "${f}")
        source=conf/$filename
        target="$f/conf/"

        echo "file: ${global_conf}, $source, target: $target"
        find ${global_conf} ${source} -type f -name '*.yml' -exec cp -- "{}" ${target} \;
    done
    ;;
esac
