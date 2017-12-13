FROM php:7-fpm-alpine

MAINTAINER Mathias Schilling <m@matchilling.com>

ENV APP_PATH /var/www/html

# Install modules
RUN apk add --update bash && \
    apk add --update git  && \
    apk add --update postgresql-dev && \
    apk add --update postgresql-client && \
    apk add --update redis  && \
    rm -rf /var/cache/apk/* && \
    docker-php-ext-install pdo pdo_pgsql

# Create app directory
RUN mkdir -p $APP_PATH
WORKDIR $APP_PATH

# Install app dependencies
RUN curl --silent --show-error https://getcomposer.org/installer | php
COPY composer.lock $APP_PATH
COPY composer.json $APP_PATH
RUN php composer.phar install

# Bundle app source
COPY . $APP_PATH

CMD ["php-fpm"]
