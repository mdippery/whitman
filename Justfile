sh: build
  docker run --rm -it --network=whitman_whitman --entrypoint=/bin/sh whitman

run etc: build
  docker run --rm --network=whitman_whitman whitman etc/{{etc}}

test: build-test
  docker run whitman-test

build:
  docker build -t whitman .

build-test:
  docker build -f docker/test/Dockerfile -t whitman-test .
