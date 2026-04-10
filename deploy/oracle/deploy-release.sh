#!/usr/bin/env bash
set -euo pipefail

if [[ $# -ne 3 ]]; then
  echo "usage: deploy-release.sh <app_dir> <release_name> <service_name>" >&2
  exit 1
fi

app_dir="$1"
release_name="$2"
service_name="$3"

release_path="${app_dir}/releases/${release_name}"
current_path="${app_dir}/current.jar"

if [[ ! -f "${release_path}" ]]; then
  echo "release jar not found: ${release_path}" >&2
  exit 1
fi

mkdir -p "${app_dir}/releases"
ln -sfn "${release_path}" "${current_path}"
sudo systemctl restart "${service_name}"
sudo systemctl --no-pager --full status "${service_name}"
