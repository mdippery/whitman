sh: build
  docker run --rm -it --network=whitman_whitman --entrypoint=/bin/sh whitman

run etc: build
  docker run --rm --network=whitman_whitman whitman etc/{{etc}}

build:
  docker build -t whitman .
