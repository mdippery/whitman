###############################################################################
# BUILDER

FROM ubuntu:14.04 AS builder

ARG MONGODB_HOST=mongo:27017

RUN apt-get update && apt-get install -y jq leiningen

COPY doc /etc/whitman
WORKDIR /etc/whitman
RUN jq ". += {\"connection\": \"$MONGODB_HOST\"}" < reddit.json > reddit.json2 \
      && jq ". += {\"connection\": \"$MONGODB_HOST\"}" < stackoverflow.json > stackoverflow.json2 \
      && mv reddit.json2 reddit.json \
      && mv stackoverflow.json2 stackoverflow.json

ENV LEIN_ROOT=true
WORKDIR /build
COPY . .
# Does not work! Can't get data.json?
RUN lein uberjar
