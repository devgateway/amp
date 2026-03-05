FROM node:16.4.0 as node

FROM node as compile-amp-state
# Copy npm install helper script
COPY docker/npm-install-with-retry.sh /usr/local/bin/npm-install-with-retry.sh
RUN chmod +x /usr/local/bin/npm-install-with-retry.sh
WORKDIR /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-state
# Copy package files first for better caching
COPY TEMPLATE/ampTemplate/node_modules/amp-state/package*.json ./
RUN --mount=type=cache,target=/root/.npm \
    npm-install-with-retry.sh
# Copy source code after dependencies are installed
COPY TEMPLATE/ampTemplate/node_modules/amp-state .

FROM node as compile-amp-translate
# Copy npm install helper script
COPY docker/npm-install-with-retry.sh /usr/local/bin/npm-install-with-retry.sh
RUN chmod +x /usr/local/bin/npm-install-with-retry.sh

# Create a directory for the app within the Docker image
WORKDIR /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-translate

# Copy only the package.json and package-lock.json files
COPY TEMPLATE/ampTemplate/node_modules/amp-translate/package*.json ./

# Install dependencies
RUN --mount=type=cache,target=/root/.npm \
    npm-install-with-retry.sh

# Copy the rest of the application code
COPY TEMPLATE/ampTemplate/node_modules/amp-translate ./

# Continue with your build steps if needed



FROM node as compile-amp-boilerplate
# Copy npm install helper script
COPY docker/npm-install-with-retry.sh /usr/local/bin/npm-install-with-retry.sh
RUN chmod +x /usr/local/bin/npm-install-with-retry.sh
WORKDIR /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-boilerplate
# Copy dependencies first
COPY --from=compile-amp-translate /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-translate ../amp-translate
# Copy package files for dependency installation
COPY TEMPLATE/ampTemplate/node_modules/amp-boilerplate/package*.json ./
COPY TEMPLATE/ampTemplate/node_modules/amp-boilerplate/.git ./
RUN --mount=type=cache,target=/root/.npm \
    npm-install-with-retry.sh
# Copy source code after dependencies are installed
COPY TEMPLATE/ampTemplate/node_modules/amp-boilerplate .
RUN npm run build

FROM node as compile-amp-filter
# Copy npm install helper script
COPY docker/npm-install-with-retry.sh /usr/local/bin/npm-install-with-retry.sh
RUN chmod +x /usr/local/bin/npm-install-with-retry.sh
WORKDIR /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-filter
# Copy dependencies first
COPY --from=compile-amp-translate /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-translate ../amp-translate
# Copy package files for dependency installation
COPY TEMPLATE/ampTemplate/node_modules/amp-filter/package*.json ./
COPY TEMPLATE/ampTemplate/node_modules/amp-filter/.git ./
RUN --mount=type=cache,target=/root/.npm \
    npm-install-with-retry.sh
# Copy source code after dependencies are installed
COPY TEMPLATE/ampTemplate/node_modules/amp-filter .
# HACK otherwise amp-filter won't compile!
COPY TEMPLATE/reamp/tools/log /tmp/amp/TEMPLATE/reamp/tools/log
RUN npm run build

FROM node as compile-amp-settings
# Copy npm install helper script
COPY docker/npm-install-with-retry.sh /usr/local/bin/npm-install-with-retry.sh
RUN chmod +x /usr/local/bin/npm-install-with-retry.sh
WORKDIR /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-settings
# Copy dependencies first
COPY --from=compile-amp-translate /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-translate ../amp-translate
# Copy package files for dependency installation
COPY TEMPLATE/ampTemplate/node_modules/amp-settings/package*.json ./
RUN --mount=type=cache,target=/root/.npm \
    npm-install-with-retry.sh
# Copy source code after dependencies are installed
COPY TEMPLATE/ampTemplate/node_modules/amp-settings .
# HACK otherwise amp-settings won't compile!
COPY TEMPLATE/reamp/tools/log /tmp/amp/TEMPLATE/reamp/tools/log
RUN npm run build

FROM node as compile-gis-layers-manager
# Copy npm install helper script
COPY docker/npm-install-with-retry.sh /usr/local/bin/npm-install-with-retry.sh
RUN chmod +x /usr/local/bin/npm-install-with-retry.sh
WORKDIR /tmp/amp/TEMPLATE/ampTemplate/node_modules/gis-layers-manager
# Copy dependencies first
COPY --from=compile-amp-translate /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-translate ../amp-translate
# Copy package files for dependency installation
COPY TEMPLATE/ampTemplate/node_modules/gis-layers-manager/package*.json ./
RUN --mount=type=cache,target=/root/.npm \
    npm-install-with-retry.sh
# Copy source code after dependencies are installed
COPY TEMPLATE/ampTemplate/node_modules/gis-layers-manager .
RUN npm run build

FROM node as compile-gis-module
# Copy npm install helper script
COPY docker/npm-install-with-retry.sh /usr/local/bin/npm-install-with-retry.sh
RUN chmod +x /usr/local/bin/npm-install-with-retry.sh
WORKDIR /tmp/amp/TEMPLATE/ampTemplate/gisModule
# Copy dependencies first (these change less frequently)
COPY --from=compile-amp-state /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-state ../node_modules/amp-state
COPY --from=compile-amp-translate /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-translate ../node_modules/amp-translate
COPY --from=compile-amp-boilerplate /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-boilerplate ../node_modules/amp-boilerplate
COPY --from=compile-amp-filter /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-filter ../node_modules/amp-filter
COPY --from=compile-amp-settings /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-settings ../node_modules/amp-settings
COPY --from=compile-gis-layers-manager /tmp/amp/TEMPLATE/ampTemplate/node_modules/gis-layers-manager ../node_modules/gis-layers-manager
# HACK otherwise amp-filter won't compile!
COPY TEMPLATE/reamp/tools/log /tmp/amp/TEMPLATE/reamp/tools/log
# Copy package files for dependency installation
COPY TEMPLATE/ampTemplate/gisModule/dev/package*.json ./dev/
RUN --mount=type=cache,target=/root/.npm \
    npm-install-with-retry.sh dev
# Copy source code after dependencies are installed
COPY TEMPLATE/ampTemplate/gisModule .
ARG SKIP_TESTS=false
RUN cd dev \
    && if [ "$SKIP_TESTS" != "true" ]; then npm run test; fi \
    && npm run build \
    && rm -rf node_modules

FROM node as compile-dashboard
# Copy npm install helper script
COPY docker/npm-install-with-retry.sh /usr/local/bin/npm-install-with-retry.sh
RUN chmod +x /usr/local/bin/npm-install-with-retry.sh
WORKDIR /tmp/amp/TEMPLATE/ampTemplate/dashboard
# Copy dependencies first (these change less frequently)
COPY --from=compile-amp-state /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-state ../node_modules/amp-state
COPY --from=compile-amp-translate /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-translate ../node_modules/amp-translate
COPY --from=compile-amp-boilerplate /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-boilerplate ../node_modules/amp-boilerplate
COPY --from=compile-amp-filter /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-filter ../node_modules/amp-filter
COPY --from=compile-amp-settings /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-settings ../node_modules/amp-settings
COPY TEMPLATE/ampTemplate/node_modules/amp-url ../node_modules/amp-url
# HACK otherwise amp-filter won't compile!
COPY TEMPLATE/reamp/tools/log /tmp/amp/TEMPLATE/reamp/tools/log
# Copy package files for dependency installation
COPY TEMPLATE/ampTemplate/dashboard/dev/package*.json ./dev/
RUN --mount=type=cache,target=/root/.npm \
    npm-install-with-retry.sh dev
# Copy source code after dependencies are installed
COPY TEMPLATE/ampTemplate/dashboard .
RUN cd dev \
    && npm run build \
    && rm -rf node_modules

FROM node as compile-reamp
# Copy npm install helper script
COPY docker/npm-install-with-retry.sh /usr/local/bin/npm-install-with-retry.sh
RUN chmod +x /usr/local/bin/npm-install-with-retry.sh
WORKDIR /tmp/amp/TEMPLATE/reamp
# Copy dependencies first
COPY --from=compile-amp-boilerplate /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-boilerplate ../ampTemplate/node_modules/amp-boilerplate
# Copy package files for dependency installation
COPY TEMPLATE/reamp/package*.json ./
RUN --mount=type=cache,target=/root/.npm \
    --mount=type=ssh \
    npm-install-with-retry.sh
# Copy source code after dependencies are installed
COPY TEMPLATE/reamp .
RUN npm run build \
    && rm -rf node_modules

FROM node as compile-reampv2
# Copy npm install helper script
COPY docker/npm-install-with-retry.sh /usr/local/bin/npm-install-with-retry.sh
RUN chmod +x /usr/local/bin/npm-install-with-retry.sh
WORKDIR /tmp/amp/TEMPLATE/reampv2
# Copy dependencies first
COPY --from=compile-amp-boilerplate /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-boilerplate ../ampTemplate/node_modules/amp-boilerplate
COPY --from=compile-amp-translate /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-translate ../ampTemplate/node_modules/amp-translate
COPY --from=compile-amp-filter /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-filter ../ampTemplate/node_modules/amp-filter
COPY --from=compile-amp-settings /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-settings ../ampTemplate/node_modules/amp-settings
# Copy package files for dependency installation - needed for npm workspaces
COPY TEMPLATE/reampv2/package*.json ./
COPY TEMPLATE/reampv2/lerna.json ./
COPY TEMPLATE/reampv2/nx.json ./
# Copy packages directory structure (needed for workspaces to resolve properly)
COPY TEMPLATE/reampv2/packages ./packages
# Patch github: references to local file: paths so npm install does not need SSH access
RUN find packages -maxdepth 2 -name "package.json" | xargs sed -i \
    -e 's|github:devgateway/amp-boilerplate|file:../../../ampTemplate/node_modules/amp-boilerplate|g' \
    -e 's|github:devgateway/amp-filter|file:../../../ampTemplate/node_modules/amp-filter|g' \
    -e 's|github:devgateway/amp-settings|file:../../../ampTemplate/node_modules/amp-settings|g'
# Remove lockfile so npm regenerates it using the patched file: references
RUN rm -f package-lock.json
RUN --mount=type=cache,target=/root/.npm \
    npm install
# Copy remaining source code after dependencies are installed
COPY TEMPLATE/reampv2 .
RUN npm run build \
    && rm -rf node_modules
FROM maven:3.8.4-jdk-8 as compile-mvn
WORKDIR /tmp/amp
# Copy pom.xml first for better Maven caching
COPY pom.xml .
#COPY amp/pom.xml amp/
# Copy only Maven dependencies (these change less frequently)
COPY --from=compile-amp-boilerplate /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-boilerplate/dist TEMPLATE/ampTemplate/node_modules/amp-boilerplate/dist
COPY --from=compile-amp-filter /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-filter/dist TEMPLATE/ampTemplate/node_modules/amp-filter/dist
COPY --from=compile-amp-settings /tmp/amp/TEMPLATE/ampTemplate/node_modules/amp-settings/dist TEMPLATE/ampTemplate/node_modules/amp-settings/dist
COPY --from=compile-gis-layers-manager /tmp/amp/TEMPLATE/ampTemplate/node_modules/gis-layers-manager/dist TEMPLATE/ampTemplate/node_modules/gis-layers-manager/dist
COPY --from=compile-gis-module /tmp/amp/TEMPLATE/ampTemplate/gisModule/dist TEMPLATE/ampTemplate/gisModule/dist
COPY --from=compile-dashboard /tmp/amp/TEMPLATE/ampTemplate/dashboard/build TEMPLATE/ampTemplate/dashboard/build
COPY --from=compile-reamp /tmp/amp/TEMPLATE/reamp TEMPLATE/reamp
COPY --from=compile-reampv2 /tmp/amp/TEMPLATE/reampv2 TEMPLATE/reampv2
# Download Maven dependencies (this layer will be cached if pom.xml doesn't change)
ARG BUILD_SOURCE
RUN --mount=type=cache,target=/root/.m2 \
    mvn -B dependency:go-offline -f pom.xml || true
# Copy source code after dependencies are downloaded
COPY . .
ARG SKIP_TESTS=false
RUN --mount=type=cache,target=/root/.m2 \
    if [ "$SKIP_TESTS" = "true" ]; then \
      mvn -B clean compile war:exploded \
        -DbuildSource=$BUILD_SOURCE \
        -Djdbc.user=amp -Djdbc.password=amp122006 -Djdbc.db=amp -Djdbc.host=db \
        -Djdbc.port=5432 -DdbName=postgresql -Djdbc.driverClassName=org.postgresql.Driver \
        -Dskip.npm -Dskip.installnodenpm -DskipTests \
        && mv target/amp exploded \
        && rm -rf target; \
    else \
      mvn -B test war:exploded \
        -DbuildSource=$BUILD_SOURCE \
        -Djdbc.user=amp -Djdbc.password=amp122006 -Djdbc.db=amp -Djdbc.host=db \
        -Djdbc.port=5432 -DdbName=postgresql -Djdbc.driverClassName=org.postgresql.Driver \
        -Dskip.npm -Dskip.installnodenpm \
        && mv target/amp exploded \
        && rm -rf target; \
    fi

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
