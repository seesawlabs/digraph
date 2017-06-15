(ns user
  (:require [clojure.pprint :refer [pprint]]
            [clojure.repl :refer :all]
            [clojure.tools.namespace.repl :refer  [refresh]]
            [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [clojure.spec.test.alpha :as stest]
            [clojure.test.check :as tc]
            [orchestra.spec.test :as st]
            [digraph.core :as core]
            [digraph.core-test :as core-test]))

(st/instrument)
