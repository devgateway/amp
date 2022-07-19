FROM maven:3.8.4-jdk-8 as base
WORKDIR /tmp/amp
COPY . .

FROM base as compile
ARG BUILD_SOURCE
RUN mkdir -p -m 0600 ~/.ssh && ssh-keyscan github.com >> ~/.ssh/known_hosts
RUN --mount=type=ssh \
    --mount=type=cache,target=/root/.m2 \
    --mount=type=cache,target=/root/.npm \
    mvn -B compile war:exploded \
      -DbuildSource=$BUILD_SOURCE \
      -Djdbc.user=amp -Djdbc.password=amp122006 -Djdbc.db=amp -Djdbc.host=db \
      -Djdbc.port=5432 -DdbName=postgresql -Djdbc.driverClassName=org.postgresql.Driver \
    && mv target/amp exploded \
    && rm -rf target \
        TEMPLATE/ampTemplate/node_modules/amp-boilerplate/node \
        TEMPLATE/ampTemplate/node_modules/amp-boilerplate/node_modules \
        TEMPLATE/ampTemplate/node_modules/gis-layers-manager/node \
        TEMPLATE/ampTemplate/node_modules/gis-layers-manager/node_modules \
        TEMPLATE/ampTemplate/node_modules/amp-settings/node \
        TEMPLATE/ampTemplate/node_modules/amp-settings/node_modules \
        TEMPLATE/ampTemplate/node_modules/amp-translate/node \
        TEMPLATE/ampTemplate/node_modules/amp-translate/node_modules \
        TEMPLATE/ampTemplate/node_modules/amp-state/node \
        TEMPLATE/ampTemplate/node_modules/amp-state/node_modules \
        TEMPLATE/ampTemplate/node_modules/amp-filter/node \
        TEMPLATE/ampTemplate/node_modules/amp-filter/node_modules \
        TEMPLATE/ampTemplate/gisModule/dev/node \
        TEMPLATE/ampTemplate/gisModule/dev/node_modules \
        TEMPLATE/ampTemplate/dashboard/dev/node \
        TEMPLATE/ampTemplate/dashboard/dev/node_modules \
        TEMPLATE/reamp/node \
        TEMPLATE/reamp/node_modules \
        TEMPLATE/reampv2/node \
        TEMPLATE/reampv2/node_modules

FROM tomcat:8.5.79-jdk8

ARG AMP_PULL_REQUEST
ARG AMP_BRANCH
ARG AMP_REGISTRY_PRIVATE_KEY

LABEL "pull-request"=$AMP_PULL_REQUEST
LABEL "branch"=$AMP_BRANCH

COPY docker/wait-for-it.sh docker/setenv.sh bin/
COPY docker/server.xml docker/tomcat-users.xml conf/

ENV AMP_REGISTRY_PRIVATE_KEY $AMP_REGISTRY_PRIVATE_KEY

RUN rm -fr /usr/local/tomcat/webapps/ROOT
COPY --from=compile /tmp/amp/exploded /usr/local/tomcat/webapps/ROOT/
