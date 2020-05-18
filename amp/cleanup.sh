#!/bin/bash

find . -type f | wc -l

rm -rf TEMPLATE/ampTemplate/node_modules/amp-boilerplate/node \
  TEMPLATE/ampTemplate/node_modules/amp-boilerplate/node_modules \
  TEMPLATE/ampTemplate/node_modules/gis-layers-manager/node \
  TEMPLATE/ampTemplate/node_modules/gis-layers-manager/node_modules \
  TEMPLATE/ampTemplate/node_modules/amp-settings/node \
  TEMPLATE/ampTemplate/node_modules/amp-settings/node_modules \
  TEMPLATE/ampTemplate/node_modules/amp-translate/node \
  TEMPLATE/ampTemplate/node_modules/amp-translate/node_modules \
  TEMPLATE/ampTemplate/node_modules/amp-state/node \
  TEMPLATE/ampTemplate/node_modules/amp-state/node_modules \
  TEMPLATE/ampTemplate/gisModule/dev/node \
  TEMPLATE/ampTemplate/gisModule/dev/node_modules \
  TEMPLATE/ampTemplate/dashboard/dev/node \
  TEMPLATE/ampTemplate/dashboard/dev/node_modules \
  TEMPLATE/reamp/node \
  TEMPLATE/reamp/node_modules

find . -type f | wc -l
