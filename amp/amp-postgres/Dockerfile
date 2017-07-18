FROM postgres:9.4
MAINTAINER Octavian Ciubotaru <ociubotaru@developmentgateway.org>

ENV GIS_MAJOR 2.3
ENV GIS_VERSION 2.3.2+dfsg-1~exp2.pgdg80+1

ENV POSTGRES_PASSWORD postgres

RUN apt-get update \
        && apt-get install -y --no-install-recommends \
                postgresql-$PG_MAJOR-postgis-$GIS_MAJOR=$GIS_VERSION \
                postgresql-$PG_MAJOR-postgis-$GIS_MAJOR-scripts=$GIS_VERSION \
        && rm -rf /var/lib/apt/lists/*

COPY restore-amp-db.sh /docker-entrypoint-initdb.d/
