# Whitman

Whitman is a simple service for sampling data from a JSON-based web API over
time. It is inspired by two other more specific projects,
[Chameleon][chameleon] and [Karmanaut][karmanaut]. Chameleon was designed to
sample Stack Overflow users' reputation over time; Karmanaut was designed to
sample Reddit users' comment and link karma scores over time. Whitman, on
the other hand, is designed to be more general. It can sample data from _any_
JSON-based web API over time, storing the results in a MongoDB database, by
simply creating and editing a JSON configuration file that declares the
structure of the target web API, which data should be sampled from it, and
how it should be stored.

## Prerequisites

1. [Leiningen][lein]
2. [MongoDB][mongodb]

  [chameleon]: https://github.com/mdippery/chameleon
  [karmanaut]: https://github.com/mdippery/karmanaut
  [lein]:      http://leiningen.org/
  [mongodb]:   http://www.mongodb.org/
