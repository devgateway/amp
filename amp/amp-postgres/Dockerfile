FROM postgres:9.4
MAINTAINER Octavian Ciubotaru <ociubotaru@developmentgateway.org>

ENV GIS_MAJOR 2.3

ENV POSTGRES_PASSWORD postgres

RUN apt-get update \
        && apt-get install -y --no-install-recommends \
                postgresql-$PG_MAJOR-postgis-$GIS_MAJOR \
                postgresql-$PG_MAJOR-postgis-$GIS_MAJOR-scripts \
        && rm -rf /var/lib/apt/lists/*

COPY restore-amp-db.sh /docker-entrypoint-initdb.d/
