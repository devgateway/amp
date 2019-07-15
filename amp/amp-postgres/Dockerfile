FROM postgres:9.4
MAINTAINER Octavian Ciubotaru <ociubotaru@developmentgateway.org>

ENV GIS_MAJOR 2.3

ENV POSTGRES_PASSWORD postgres

RUN apt-get update \
        && apt-get install -y --no-install-recommends \
                postgresql-$PG_MAJOR-postgis-$GIS_MAJOR \
                postgresql-$PG_MAJOR-postgis-$GIS_MAJOR-scripts \
        && rm -rf /var/lib/apt/lists/*

RUN sed -ri -e "s:#log_directory = 'pg_log':log_directory = '/var/log/postgresql':" \
                -e 's/#logging_collector = off/logging_collector = on/' \
                -e 's/#log_file_mode = 0600/log_file_mode = 0644/' \
                /usr/share/postgresql/postgresql.conf.sample

COPY restore-amp-db.sh /docker-entrypoint-initdb.d/
