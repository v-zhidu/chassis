# Chassis Operations Manual

## Standalone

1. Chassis is now installed in folder `build-target`.

```
cd build-target
```

2. Edit `config/service` for choosing what services you want to deploy or adding more instances.

```text
chassis-middleware-id-generator:9781
chassis-middleware-sample:19781
```

3. Edit the default application configuration `config/application/` if you need.

4. Start cluster locally.

```bash
./bin/start_cluster.sh`
```

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