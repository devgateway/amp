FROM node:16.4.0 as node

FROM node as compile-amp-state
WORKDIR /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-state
COPY /TEMPLATE/ampTemplate/node_modules/amp-state .
RUN --mount=type=cache,target=/root/.npm \
    npm ci

FROM node as compile-amp-translate
WORKDIR /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-translate
COPY /TEMPLATE/ampTemplate/node_modules/amp-translate .
RUN --mount=type=cache,target=/root/.npm \
    npm ci

FROM node as compile-amp-boilerplate
WORKDIR /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-boilerplate
COPY /TEMPLATE/ampTemplate/node_modules/amp-boilerplate .
COPY --from=compile-amp-translate /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-translate ../amp-translate
RUN --mount=type=cache,target=/root/.npm \
    npm ci \
    && npm run build

FROM node as compile-amp-filter
WORKDIR /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-filter
COPY --from=compile-amp-translate /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-translate ../amp-translate
COPY /TEMPLATE/ampTemplate/node_modules/amp-filter .
# HACK otherwise amp-filter won't compile!
COPY /TEMPLATE/reamp/tools/log /tmp/amp/TEMPLATE/reamp/tools/log
RUN --mount=type=cache,target=/root/.npm \
    npm ci \
    && npm run build

FROM node as compile-amp-settings
WORKDIR /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-settings
COPY --from=compile-amp-translate /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-translate ../amp-translate
COPY /TEMPLATE/ampTemplate/node_modules/amp-settings .
RUN --mount=type=cache,target=/root/.npm \
    npm ci \
    && npm run build

FROM node as compile-gis-layers-manager
WORKDIR /tmp/amp/TEMPLATE/ampTemplate/node_modules/gis-layers-manager
COPY --from=compile-amp-translate /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-translate ../amp-translate
COPY /TEMPLATE/ampTemplate/node_modules/gis-layers-manager .
RUN --mount=type=cache,target=/root/.npm \
    npm ci \
    && npm run build

FROM node as compile-gis-module
WORKDIR /tmp/amp/TEMPLATE/ampTemplate/gisModule
COPY --from=compile-amp-state /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-state ../node_modules/amp-state
COPY --from=compile-amp-translate /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-translate ../node_modules/amp-translate
COPY --from=compile-amp-boilerplate /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-boilerplate ../node_modules/amp-boilerplate
COPY --from=compile-amp-filter /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-filter ../node_modules/amp-filter
# HACK otherwise amp-filter won't compile!
COPY /TEMPLATE/reamp/tools/log /tmp/amp/TEMPLATE/reamp/tools/log
COPY --from=compile-amp-settings /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-settings ../node_modules/amp-settings
COPY --from=compile-gis-layers-manager /tmp/amp/TEMPLATE/ampTemplate/node_modules/gis-layers-manager ../node_modules/gis-layers-manager
COPY /TEMPLATE/ampTemplate/gisModule .
RUN --mount=type=cache,target=/root/.npm \
    cd dev \
    && npm ci \
    && npm run test \
    && npm run build \
    && rm -rf node_modules

FROM node as compile-dashboard
WORKDIR /tmp/amp/TEMPLATE/ampTemplate/dashboard
COPY --from=compile-amp-state /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-state ../node_modules/amp-state
COPY --from=compile-amp-translate /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-translate ../node_modules/amp-translate
COPY --from=compile-amp-boilerplate /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-boilerplate ../node_modules/amp-boilerplate
COPY --from=compile-amp-filter /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-filter ../node_modules/amp-filter
# HACK otherwise amp-filter won't compile!
COPY /TEMPLATE/reamp/tools/log /tmp/amp/TEMPLATE/reamp/tools/log
COPY --from=compile-amp-settings /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-settings ../node_modules/amp-settings
COPY /TEMPLATE/ampTemplate/node_modules/amp-url ../node_modules/amp-url
COPY /TEMPLATE/ampTemplate/dashboard .
RUN --mount=type=cache,target=/root/.npm \
    cd dev \
    && npm ci \
    && npm run build \
    && rm -rf node_modules

FROM node as compile-reamp
WORKDIR /tmp/amp/TEMPLATE/reamp
COPY --from=compile-amp-boilerplate /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-boilerplate ../ampTemplate/node_modules/amp-boilerplate
COPY /TEMPLATE/reamp .
RUN --mount=type=cache,target=/root/.npm \
    --mount=type=ssh \
    npm ci \
    && npm run build \
    && rm -rf node_modules

FROM node as compile-reampv2
WORKDIR /tmp/amp/TEMPLATE/reampv2
COPY --from=compile-amp-boilerplate /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-boilerplate ../ampTemplate/node_modules/amp-boilerplate
COPY --from=compile-amp-filter /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-filter ../ampTemplate/node_modules/amp-filter
COPY --from=compile-amp-settings /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-settings ../ampTemplate/node_modules/amp-settings
COPY /TEMPLATE/reampv2 .
RUN --mount=type=cache,target=/root/.npm \
    --mount=type=ssh \
    npm ci \
    && npm run build \
    && rm -rf node_modules

FROM maven:3.8.4-jdk-8 as compile-mvn
WORKDIR /tmp/amp
COPY . .
COPY --from=compile-amp-boilerplate /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-boilerplate/dist TEMPLATE/ampTemplate/node_modules/amp-boilerplate/dist
COPY --from=compile-amp-filter /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-filter/dist TEMPLATE/ampTemplate/node_modules/amp-filter/dist
COPY --from=compile-amp-settings /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-settings/dist TEMPLATE/ampTemplate/node_modules/amp-settings/dist
COPY --from=compile-gis-layers-manager /tmp/amp/TEMPLATE/ampTemplate/node_modules/gis-layers-manager/dist TEMPLATE/ampTemplate/node_modules/gis-layers-manager/dist
COPY --from=compile-gis-module /tmp/amp/TEMPLATE/ampTemplate/gisModule/dist TEMPLATE/ampTemplate/gisModule/dist
COPY --from=compile-dashboard /tmp/amp/TEMPLATE/ampTemplate/dashboard/build TEMPLATE/ampTemplate/dashboard/build
COPY --from=compile-reamp /tmp/amp/TEMPLATE/reamp TEMPLATE/reamp
COPY --from=compile-reampv2 /tmp/amp/TEMPLATE/reampv2/build TEMPLATE/reampv2/build
ARG BUILD_SOURCE
RUN --mount=type=cache,target=/root/.m2 \
    mvn -B test war:exploded \
      -DbuildSource=$BUILD_SOURCE \
      -DfailIfNoTests=false \
      -Djdbc.user=amp -Djdbc.password=amp122006 -Djdbc.db=amp -Djdbc.host=db \
      -Djdbc.port=5433 -DdbName=postgresql -Djdbc.driverClassName=org.postgresql.Driver \
      -Dskip.npm -Dskip.installnodenpm \
    && mv target/amp exploded \
    && rm -rf target

FROM tomcat:8.5.79-jdk8

RUN apt-get update \
    && apt-get install -y apt-transport-https ca-certificates curl gnupg --no-install-recommends \
    && curl -sSL https://dl.google.com/linux/linux_signing_key.pub | apt-key add - \
    && echo "deb https://dl.google.com/linux/chrome/deb/ stable main" > /etc/apt/sources.list.d/google-chrome.list \
    && apt-get update \
    && apt-get install -y google-chrome-stable fontconfig fonts-ipafont-gothic fonts-wqy-zenhei fonts-thai-tlwg fonts-kacst fonts-symbola fonts-noto fonts-freefont-ttf --no-install-recommends \
    && apt-get purge --auto-remove -y curl gnupg \
    && rm -rf /var/lib/apt/lists/*

ENV CHROME_PATH /opt/google/chrome
ENV CHROME_NO_SANDBOX 1

COPY docker/wait-for-it.sh docker/setenv.sh bin/
COPY docker/server.xml docker/tomcat-users.xml conf/

ARG AMP_PULL_REQUEST
ARG AMP_BRANCH
ARG AMP_REGISTRY_PRIVATE_KEY

LABEL "pull-request"=$AMP_PULL_REQUEST
LABEL "branch"=$AMP_BRANCH

ENV AMP_REGISTRY_PRIVATE_KEY $AMP_REGISTRY_PRIVATE_KEY

RUN rm -fr /usr/local/tomcat/webapps/ROOT
COPY --from=compile-mvn /tmp/amp/exploded /usr/local/tomcat/webapps/ROOT/
