# Oracle VM Deploy

This directory contains a minimal `systemd + GitHub Actions + scp/ssh` deployment path for KitchenComplianceHub on an Oracle Linux or Ubuntu VM.

## Server layout

Use a stable application directory for releases and separate stable data directories for CSV persistence.

```text
/opt/kitchencompliancehub
  /releases
  current.jar -> /opt/kitchencompliancehub/releases/kitchen-compliance-hub-<sha>.jar

/etc/kitchencompliancehub/kitchencompliancehub.env

/var/lib/kitchencompliancehub
  /attribution
  /leads
  /ops
```

## First-time server setup

Create the service user and directories:

```bash
sudo useradd --system --home /opt/kitchencompliancehub --shell /usr/sbin/nologin kitchencompliancehub
sudo mkdir -p /opt/kitchencompliancehub/releases
sudo mkdir -p /etc/kitchencompliancehub
sudo mkdir -p /var/lib/kitchencompliancehub/attribution
sudo mkdir -p /var/lib/kitchencompliancehub/leads
sudo mkdir -p /var/lib/kitchencompliancehub/ops
sudo chown -R kitchencompliancehub:kitchencompliancehub /opt/kitchencompliancehub
sudo chown -R kitchencompliancehub:kitchencompliancehub /var/lib/kitchencompliancehub
```

Install the environment file:

```bash
sudo cp deploy/oracle/systemd/kitchencompliancehub.env.example /etc/kitchencompliancehub/kitchencompliancehub.env
sudo chown root:root /etc/kitchencompliancehub/kitchencompliancehub.env
sudo chmod 640 /etc/kitchencompliancehub/kitchencompliancehub.env
```

Install the service:

```bash
sudo cp deploy/oracle/systemd/kitchencompliancehub.service /etc/systemd/system/kitchencompliancehub.service
sudo systemctl daemon-reload
sudo systemctl enable kitchencompliancehub
```

Edit `/etc/kitchencompliancehub/kitchencompliancehub.env` before first start:

- `APP_SITE_BASE_URL`
- `APP_ATTRIBUTION_LOG_DIR`
- `APP_LEAD_LOG_DIR`
- `APP_OPS_AUDIT_DIR`

If you want to use custom JVM flags, change `JAVA_OPTS` and then update `ExecStart` to `ExecStart=/usr/bin/java $JAVA_OPTS -jar /opt/kitchencompliancehub/current.jar`.

## GitHub Actions secrets and variables

Secrets:

- `ORACLE_HOST`
- `ORACLE_USER`
- `ORACLE_SSH_PRIVATE_KEY`
- `ORACLE_SSH_PORT` (optional, defaults to `22`)

Variables:

- `ORACLE_APP_DIR` (optional, defaults to `/opt/kitchencompliancehub`)
- `ORACLE_SERVICE_NAME` (optional, defaults to `kitchencompliancehub`)

## Why CSV survives redeploys

The JAR is deployed under `/opt/kitchencompliancehub`, but attribution, leads, and ops snapshots live under `/var/lib/kitchencompliancehub`. A new release only changes `current.jar`, so the CSV files persist across restarts and redeploys.
