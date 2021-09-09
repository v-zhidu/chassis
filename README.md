# Chassis

[![](https://github.com/v-zhidu/chassis/actions/workflows/maven.yml/badge.svg?branch=main)](https://github.com/v-zhidu/chassis/actions/workflows/maven.yml)
[![](https://sonarcloud.io/api/project_badges/measure?branch=main&project=v-zhidu_chassis&metric=alert_status)](https://sonarcloud.io/dashboard?id=v-zhidu_chassis)
[![](https://sonarcloud.io/api/project_badges/measure?branch=main&project=v-zhidu_chassis&metric=security_rating)](https://sonarcloud.io/dashboard?id=v-zhidu_chassis)

[![](https://sonarcloud.io/api/project_badges/measure?branch=main&project=v-zhidu_chassis&metric=coverage)](https://sonarcloud.io/dashboard?id=v-zhidu_chassis)
[![](https://sonarcloud.io/api/project_badges/measure?branch=main&project=v-zhidu_chassis&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=v-zhidu_chassis) 
[![](https://sonarcloud.io/api/project_badges/measure?branch=main&project=v-zhidu_chassis&metric=bugs)](https://sonarcloud.io/dashboard?id=v-zhidu_chassis)
[![](https://sonarcloud.io/api/project_badges/measure?branch=main&project=v-zhidu_chassis&metric=code_smells)](https://sonarcloud.io/dashboard?id=v-zhidu_chassis)
[![](https://sonarcloud.io/api/project_badges/measure?branch=main&project=v-zhidu_chassis&metric=ncloc)](https://sonarcloud.io/dashboard?id=v-zhidu_chassis)

### Features

## Building Chassis from source

Prerequisites for building code:

* Unix-like environment (we use Linux, Mac OS X, Cygwin)
* git
* Maven (we recommend version 3.2.5 and require at least 3.1.1)
* Java 11

```
git clone git@repo.advai.net:p2p/chassis.git

cd chassis

./mvnw clean package -DskipTests
```

Now Chassis installed in `build-target`


## Developing

### IntelliJ IDEA

We recommend to use IntelliJ for developing the sourcecode.

* IntelliJ download: [https://www.jetbrains.com/idea/](https://www.jetbrains.com/idea/)

## Documentation

For now, the documentation of project located in the `docs/` directory of the source code

[How to build documentation](_docs/README.md)
