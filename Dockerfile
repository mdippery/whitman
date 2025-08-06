###############################################################################
# BUILDER

FROM alpine:3.22 AS builder

ARG MONGODB_HOST=mongo:27017

RUN apk update && apk add jq leiningen

COPY examples /etc/whitman
WORKDIR /etc/whitman
RUN jq ". += {\"connection\": \"$MONGODB_HOST\"}" < reddit.json > reddit.json2 \
      && jq ". += {\"connection\": \"$MONGODB_HOST\"}" < stackoverflow.json > stackoverflow.json2 \
      && mv reddit.json2 reddit.json \
      && mv stackoverflow.json2 stackoverflow.json

WORKDIR /build
COPY . .
RUN lein uberjar


###############################################################################
# RUNNER

FROM alpine:3.22 AS runner

RUN apk update && apk add openjdk21-jre-headless
RUN addgroup -S whitman && adduser -S whitman -G whitman

USER whitman

WORKDIR /app
COPY --from=builder --chown=whitman:whitman /build/target/uberjar/whitman-standalone.jar .
COPY --from=builder --chown=whitman:whitman /etc/whitman etc

ENTRYPOINT ["java", "-XX:UseSVE=0", "-jar", "whitman-standalone.jar"]
