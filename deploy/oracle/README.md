# Oracle VM Deploy

This directory contains a minimal `systemd + GitHub Actions + scp/ssh` deployment path for KitchenRuleHub on an Oracle Linux or Ubuntu VM.

## Server layout

Use a stable application directory for releases and separate stable data directories for CSV persistence.

```text
/opt/kitchenrulehub
  /releases
  current.jar -> /opt/kitchenrulehub/releases/kitchen-rule-hub-<sha>.jar

/etc/kitchenrulehub/kitchenrulehub.env

/var/lib/kitchenrulehub
  /attribution
  /leads
  /ops
```

## First-time server setup

Create the service user and directories:

```bash
sudo useradd --system --home /opt/kitchenrulehub --shell /usr/sbin/nologin kitchenrulehub
sudo mkdir -p /opt/kitchenrulehub/releases
sudo mkdir -p /etc/kitchenrulehub
sudo mkdir -p /var/lib/kitchenrulehub/attribution
sudo mkdir -p /var/lib/kitchenrulehub/leads
sudo mkdir -p /var/lib/kitchenrulehub/ops
sudo chown -R kitchenrulehub:kitchenrulehub /opt/kitchenrulehub
sudo chown -R kitchenrulehub:kitchenrulehub /var/lib/kitchenrulehub
```

Install the environment file:

```bash
sudo cp deploy/oracle/systemd/kitchenrulehub.env.example /etc/kitchenrulehub/kitchenrulehub.env
sudo chown root:root /etc/kitchenrulehub/kitchenrulehub.env
sudo chmod 640 /etc/kitchenrulehub/kitchenrulehub.env
```

Install the service:

```bash
sudo cp deploy/oracle/systemd/kitchenrulehub.service /etc/systemd/system/kitchenrulehub.service
sudo systemctl daemon-reload
sudo systemctl enable kitchenrulehub
```

Edit `/etc/kitchenrulehub/kitchenrulehub.env` before first start:

- `SPRING_PROFILES_ACTIVE` should stay `prod`
- `APP_SITE_BASE_URL`
- `APP_ADMIN_USERNAME`
- `APP_ADMIN_PASSWORD`
- `APP_ATTRIBUTION_LOG_DIR`
- `APP_LEAD_LOG_DIR`
- `APP_OPS_AUDIT_DIR`

The systemd unit also pins `SPRING_PROFILES_ACTIVE=prod` so the app boots with the production profile even if the env file is edited later.

If you want to use custom JVM flags, change `JAVA_OPTS` and then update `ExecStart` to `ExecStart=/usr/bin/java $JAVA_OPTS -jar /opt/kitchenrulehub/current.jar`.

## GitHub Actions secrets and variables

Secrets:

- `ORACLE_HOST`
- `ORACLE_USER`
- `ORACLE_SSH_PRIVATE_KEY`
- `ORACLE_SSH_PORT` (optional, defaults to `22`)

Variables:

- `ORACLE_APP_DIR` (optional, defaults to `/opt/kitchenrulehub`)
- `ORACLE_SERVICE_NAME` (optional, defaults to `kitchenrulehub`)

## Why CSV survives redeploys

The JAR is deployed under `/opt/kitchenrulehub`, but attribution, leads, and ops snapshots live under `/var/lib/kitchenrulehub`. A new release only changes `current.jar`, so the CSV files persist across restarts and redeploys.
