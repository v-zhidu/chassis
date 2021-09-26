#!/usr/bin/env bash
#set -e

#################################################################################
usage() {
  echo "Usage:"
  echo "  ./build_image.sh <command> [options]"
  echo ""
  echo "Command:"
  echo "  build     Build docker images."
  echo "  publish   Publish images to a docker registry."
  echo ""
  echo "General Options:"
  echo "  -h        Show helps"
  exit 0
}

while getopts ":h" opt; do
    case ${opt} in
        h )
           usage
        ;;
        \? )
            echo "Invalid Option: -$OPTARG" 1>&2
            exit 1
        ;;
    esac
done
shift $((OPTIND -1))

subcommand=$1; shift

install_pack_cli() {
    ## Check pack if available
    if ! type pack > /dev/null 2> /dev/null; then
      echo "Installing Buildpacks CLI from https://buildpacks.io........"
      if [[ "$OSTYPE" == "darwin"* ]]; then
        (brew install buildpacks/tap/pack)
      else
        (curl -sSL "https://github.com/buildpacks/pack/releases/download/v0.21.1/pack-v0.21.1-linux.tgz" | sudo tar -C /usr/local/bin/ --no-same-owner -xzv pack)
      fi
    else
      echo "Buildpacks is already installed."
    fi
}

case "${subcommand}" in
  (build)
    while getopts ":p:" opt; do
      case ${opt} in
        p )
          image_prefix=$OPTARG
          ;;
        \? )
          echo "Invalid Option: -$OPTARG" 1>&2
          echo "Options:"
          echo "  -p        Image prefix"
          exit 1
          ;;
      esac
    done
    shift $((OPTIND -1))

    install_pack_cli
    pack config default-builder paketobuildpacks/builder:base
    pack builder suggest
    cd app/

    for filename in *.jar; do
        app_name=`echo $filename | rev | cut -d "-" -f3-$n | rev`
        app_version=`echo $filename | rev | cut -d "-" -f 1-2 | cut -d '.' -f 2-$n | rev`
        image_name=$([ "${image_prefix}" ] && echo "${image_prefix}/${app_name}:${app_version}" ||echo "${app_name}:${app_version}")

        echo "Building image ----> ${image_name}"
        pack build $image_name --path $filename
    done
  ;;
esac