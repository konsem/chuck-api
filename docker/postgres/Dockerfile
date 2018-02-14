FROM postgres:9.4.15-alpine

MAINTAINER Mathias Schilling <m@matchilling.com>

# Expect AWS CLI credentials & postgres connection details to be passed in as build arguments
ARG AWS_ACCESS_KEY_ID
ARG AWS_SECRET_ACCESS_KEY
ARG AWS_DEFAULT_REGION

ARG POSTGRES_DB
ARG POSTGRES_USER
ARG POSTGRES_PASSWORD

RUN apk add --no-cache --update \
    python \
    python-dev \
    py-pip \
    build-base

# Install AWS CLI tools
RUN pip install awscli

# Set default connection for psql
ENV PGHOST localhost
ENV PGPORT 5432
ENV PGUSER postgres
ENV PGPASSWORD postgres

# Download latest db backup from s3
RUN aws s3 cp s3://io.chucknorris.api/database/latest.sql /docker-entrypoint-initdb.d/latest.sql
