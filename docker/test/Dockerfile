FROM alpine:3.22

RUN apk add leiningen

COPY . /build
WORKDIR /build

# Yes, we have to set both of them.
ENV LEIN_JVM_OPTS=-XX:UseSVE=0
ENV JVM_OPTS=$LEIN_JVM_OPTS

ENTRYPOINT ["lein", "test"]
