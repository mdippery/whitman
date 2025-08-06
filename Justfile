# List all available tasks
[private]
default:
  @just -l

# Open up a shell into the Docker container
sh: build
  docker run --rm -it --network=whitman_whitman --entrypoint=/bin/sh whitman

# Run whitman using the specified config
run etc: build
  docker run --rm --network=whitman_whitman whitman etc/{{etc}}

# Test whitmanj 
test: build-test
  docker run whitman-test

# Build the Docker image
build:
  docker build -t whitman .

# Build the Docker image for running the test suite
build-test:
  docker build -f docker/test/Dockerfile -t whitman-test .
