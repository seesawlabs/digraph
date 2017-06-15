(ns digraph.core-test
  (:require [clojure.test :refer :all]
            [digraph.core :as d]
            [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as stest]
            [clojure.test.check :as tc]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.properties :as prop]))

(def degree-sum-property
  (prop/for-all [g (s/gen ::d/digraph)]
                (let [verts (:nodes g)
                      indegree-sum (apply + (map (partial d/indegree g) verts))
                      outdegree-sum (apply + (map (partial d/outdegree g) verts))
                      total-edges (count (:edges g))]
                  (= indegree-sum outdegree-sum total-edges))))

(defspec indegree-outdegree 25 degree-sum-property)

(def topo-ordinality-property
  (prop/for-all [g (s/gen ::d/digraph)]
                (let [topo (d/topological-sort g)]
                  (for [[u v] (:edges g)]
                    (< (.indexOf topo u)
                       (.indexOf topo v))))))

(defspec topological-sort-ordinality 50 topo-ordinality-property)
