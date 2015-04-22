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

Right now, Whitman is geared towards sampling a piece of data for a set of
users of an API (for example, Stack Overflow reputation or Reddit karma). It
may be generalized in the future to sample data points that are not
user-specific, but it is currently not designed to do that.

## Prerequisites

1. [Leiningen][lein]
2. [MongoDB][mongodb]

## Usage

### Building

Before you can sample data, you must first create the universe...or at least an
executable JAR file. You can build a JAR file containing all necessary `.class`
files with [lein][lein]:

    $ lein uberjar

### Setup

#### Database

1. Create a [MongoDB][mongodb] database (you may name it whatever you want).
2. Populate the `users` collection in your MongoDB database with the IDs of
   the users whose data you want to record, in the following format:

        {_id: <user ID>}

#### Configuration

Next, you must create the crawler configuration file. Some samples are given in
the `doc/` directory. Configuration files are in JSON. They may contain the
following keys (not all keys are necessary, and if they are not present in your
configuration, defaults will be used; if a default is not specified, the key is
required):

<table>
  <tr>
    <th>Key</th>
    <th>Purpose</th>
    <th>Default</th>
  </tr>
  <tr>
    <td><code>database</code></td>
    <td>MongoDB database name</td>
    <td><em>Required</em></td>
  </tr>
  <tr>
    <td><code>collection</code></td>
    <td>Name of MongoDB collection where samples should be stored</td>
    <td><em>Required</em></td>
  </tr>
  <tr>
    <td><code>user-agent</code></td>
    <td>HTTP User-Agent to use when making HTTP requests</td>
    <td><code>whitman/&lt;VERSION&gt;</code></td>
  </tr>
  <tr>
    <td><code>source</code></td>
    <td>URL from which samples should be pulled. It should contain one
        parameter that should be substituted with a user (or record) ID; this
        parameter can be denoted with the placeholder `%s`.</td>
    <td><em>Required</em></td>
  </tr>
  <tr>
    <td><code>records</code></td>
    <td>MongoDB keypath where records that should be sampled are pulled</td>
    <td><em>Required</em></td>
  </tr>
  <tr>
    <td><code>data</code></td>
    <td>Data points that should be crawled</td>
    <td>(See next table)</td>
  </tr>
</table>

The <code>data</code> specifies the data that should be crawled. It takes the
following keys:

<table>
  <tr>
    <th>Key</th>
    <th>Purpose</th>
    <th>Default</th>
  </tr>
  <tr>
    <td><code>path</code>
    <td>The keypath to the data that should be recorded from the JSON
        response.</td>
    <td><em>Required</em></td>
  </tr>
  <tr>
    <th><code>key</code>
    <td>Document key that the sampled data should be stored under</td>
    <td><em>Required</em></td>
  </tr>
</table>

##### Keypaths

Keypaths are period-separated paths specifying how the crawler should store or
retrieve various pieces of data. They are currently used in two ways: When
specifying the records, stored in MongoDB, that should be polled for data, and
for describing how to retrieve data samples from a JSON response.

The first keypath specifies what records should be crawled. It is in the
format `<collection>.<field>` and specifies the MongoDB collection and field
that are used to construct a crawlable URL for a given record. For example,
if you want to crawl a list of users stored in a `users` collection, with their
IDs specified in the `_id` field, the keypath would be `users._id`.

Keypaths are also used to describe how data should be sampled from a JSON
response. They are a period-separated path to the key that should be sampled.
For example, say you wanted to sample `link_karma` from the following API
response:

    {
      "data": {
        "link_karma": 1000
      }
    }

The keypath would be `data.link_karma`.

If a key is an array, you can specify an element of the array using an integer.
For example, if you wanted to sample `reputation` from the following API
response:

    {
      "items": [
        {
          "reputation": 150000
        }
      ]
    }

You would use the keypath `items.0.reputation`.

### Running

Once you have built Whitman, you can run it using

    $ java -jar target/uberjar/whitman-standalone.jar path/to/config.json

where `path/to/config.json` is, of course, the path to your JSON configuration
file.

  [chameleon]: https://github.com/mdippery/chameleon
  [karmanaut]: https://github.com/mdippery/karmanaut
  [lein]:      http://leiningen.org/
  [mongodb]:   http://www.mongodb.org/
