# digraph

[![Build Status](https://travis-ci.org/seesawlabs/digraph.svg?branch=master)](https://travis-ci.org/seesawlabs/digraph)

Directed graph library written in pure Clojure. Many algorithms have yet to be written, but
it's polished enough to publish and open to the community. Pull requests encouraged!

Features so far:

- Full `clojure.spec` coverage, ready to be extended
- Topological sort via Kahn's algorithm
- Cycle detection
- Indegree and outdegree
- Sink/source/internal/stranded node operations

The use of specs does indeed require Clojure version `>= 1.9.0`.

## Usage

[![Clojars Project](https://img.shields.io/clojars/v/digraph.svg)](https://clojars.org/digraph)

Require the namespace:

```Clojure
(require '[digraph.core :as d])
```

Define a graph:

```Clojure
(def g (make-digraph #{1 2 3} [1 2] [1 3] [1 4]))

; Check that the graph conforms to the spec
(s/valid? ::d/digraph g)
=> true

; Acyclic?
(s/valid? ::d/dag g)
=> true
```

Examples:

```Clojure
; Find all the sink nodes
(filter #(d/sink? g %) (:nodes g))
=> (3)

; Sort nodes topologically
(d/topological-sort g)
=> [1 2 3]

; Detect cycles
(d/cyclic? g)
=> false
```

Check the [core](./src/digraph/core.clj) namespace for other possible operations, as well as
the `clojure.spec` definitions. These can be very helpful for extending directed graphs into,
say, a network. For an example, check out [kranium](https://github.com/seesawlabs/kranium).

## Development

Refresh all namespaces in dependency order:

```Clojure
; At the REPL
(refresh)
```

All `digraph` namespaces are available from the `user` namespace by their short names:

```Clojure
; digraph.core/some-fn would be
(core/some-fn)
```

Auto run the tests upon file change:

```
lein auto test
```

The `user` namespace automatically calls `(st/instrument)`. Any defined specs will be
instrumented while working at the REPL. Note that the `user` namespace is _not_ loaded
in production builds; it is a development dependency only.

## License

Copyright © 2017 Calvin Sauer

Distributed under the Eclipse Public License version 1.0
