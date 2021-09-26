
#!/usr/bin/env bash
################################################################################
#
# Setup Zookeeper Server Cluster
#
################################################################################
set -e

CUR_DIR=`dirname "${0}"`
SYMLINK_RESOLVED_BIN=`cd "${CUR_DIR}"; pwd -P`
WORK_HOME=`dirname "$SYMLINK_RESOLVED_BIN"`
OPT_DIR=$WORK_HOME/opt

mkdir -p "${OPT_DIR}"

cd ${OPT_DIR}

ZOOKEEPER_QUANTUM="3"
ZOOKEEPER_VERSION="3.6.3"
ZOOKEEPER_BINARY="apache-zookeeper-$ZOOKEEPER_VERSION-bin.tar.gz"
ZOOKEEPER_MIRROR_SITE="https://downloads.apache.org/zookeeper"
ZOOKEEPER_TARGET="$ZOOKEEPER_MIRROR_SITE/zookeeper-$ZOOKEEPER_VERSION/$ZOOKEEPER_BINARY"

WORKING_DIR=$PWD


# Cleanup previous installation.
echo "================================================================"
echo "Cleanup previous installation..."
#kill $(ps aux | grep 'zookeeper' | grep -v 'grep' | awk '{print $2}') | xargs kill > /dev/null 2>&1
if ls zookeeper-* &>/dev/null; then
    rm -rf zookeeper-*
fi
echo "Done!"

## Fetch the target zookeeper binary.
echo "================================================================"
echo "Downloading $ZOOKEEPER_BINARY..."
wget $ZOOKEEPER_TARGET -O $ZOOKEEPER_BINARY
echo "Done!"

# Prepare zookeeper quantum.
echo "================================================================"
echo "Extracting $ZOOKEEPER_BINARY..."
for ((i=1; i<=$ZOOKEEPER_QUANTUM; i++))
do
	echo "Preparing zookeeper instance $i..."
	tar xvf $ZOOKEEPER_BINARY > /dev/null 2>&1
	mv apache-zookeeper-$ZOOKEEPER_VERSION-bin zookeeper-0$i
	mkdir zookeeper-0$i/data
	cat << EOF > zookeeper-0$i/conf/zoo.cfg
# The number of milliseconds of each tick
tickTime=2000
# The number of ticks that the initial
# synchronization phase can take
initLimit=10
# The number of ticks that can pass between
# sending a request and getting an acknowledgement
syncLimit=5
# the directory where the snapshot is stored.
# do not use /tmp for storage, /tmp here is just
# example sakes.
dataDir=$WORKING_DIR/zookeeper-0$i/data
# the port at which the clients will connect
clientPort=$((2180+$i))
# the maximum number of client connections.
# increase this if you need to handle more clients
#maxClientCnxns=60
#
# Be sure to read the maintenance section of the
# administrator guide before turning on autopurge.
#
# http://zookeeper.apache.org/doc/current/zookeeperAdmin.html#sc_maintenance
#
# The number of snapshots to retain in dataDir
#autopurge.snapRetainCount=3
# Purge task interval in hours
# Set to "0" to disable auto purge feature
#autopurge.purgeInterval=1
#server.1=localhost:2891:3891
EOF
done

for ((j=1; j<=$ZOOKEEPER_QUANTUM; j++))
do
	for ((k=1; k<=$ZOOKEEPER_QUANTUM; k++))
	do
		echo "server.$k=localhost:$((2887+$k)):$((3887+$k))" >> zookeeper-0$j/conf/zoo.cfg
	done

	echo "$j" >> zookeeper-0$j/data/myid
done

echo "Done!"

# Start zookeeper quantum.
echo "================================================================"
echo "Starting zookeeper quantum..."
for ((m=1; m<=$ZOOKEEPER_QUANTUM; m++))
do
	echo "Starting zookeeper instance $m..."
	bash zookeeper-0$m/bin/zkServer.sh start
done
echo "Done!"
rm $ZOOKEEPER_BINARY