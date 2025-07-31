run: build
  docker run -it whitman

build:
  docker build -t whitman .
