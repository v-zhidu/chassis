# Chassis

{Description}

### Features

* Built on Spring Cloud architecture，support both standalone mode and cluster with service discovery.
* Built-in file storage service (filesystem, OSS)

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
