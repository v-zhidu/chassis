# Chassis

[![](https://github.com/v-zhidu/chassis/actions/workflows/maven.yml/badge.svg?branch=main)](https://github.com/v-zhidu/chassis/actions/workflows/maven.yml)
[![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-black.svg)](https://sonarcloud.io/summary/new_code?id=io.vzhidu%3Achassis)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=io.vzhidu%3Achassis&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=io.vzhidu%3Achassis)

## Features

## Building

Prerequisites for building code:

* Unix-like environment (we use Linux, Mac OS X, Cygwin)
* git
* Maven (we recommend version 3.2.5 and require at least 3.1.1)
* Java 11

```bash
git clone https://github.com/v-zhidu/chassis.git & cd chassis

./mvnw -s .mvn/settings.xml clean package -DskipTests
```

## Deployment

### Standalone

1. Chassis is now installed in folder `build-target`.

```
cd build-target
```

2. Edit `config/service` for choosing what services you want to deploy or adding more instances.

```text
chassis-middleware-id-generator-server:9781
chassis-middleware-sample-server:19781
```

3. Edit the default application configuration `config/application/` if you need.

4. Start cluster locally.

```bash
./bin/start_cluster.sh`
```

Send a test request: 

```bash
curl -v 'http://127.0.0.1:19781/snowflakes/leaf-segment-test' | jq
````

5. Append an instance to exist cluster

```bash
./bin/appctl.sh [app] [port]
```

6. Remove an instance from a cluster

```bash
./bin/appctl.stop [app]
```

7. Stop the cluster.

```bash
./bin/stop_cluster.sh
```

### Build Docker Image

```bash
./bin/build_image.sh [build] [publish] -p [prefix]
```

## [WIP] Developing

### IntelliJ IDEA

We recommend to use IntelliJ for developing the sourcecode.

* IntelliJ download: [https://www.jetbrains.com/idea/](https://www.jetbrains.com/idea/)

## [WIP] Documentation
