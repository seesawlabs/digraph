(ns digraph.core
  "Directed graph operations and specifications."
  (:require [clojure.set :as cset]
            [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]))

;""""""""""""""""""""""""""""""""""""""""
;
; Directed graph operations
;
;""""""""""""""""""""""""""""""""""""""""
(defn adj?
  "Returns true if x is adjacent to y in the given graph, where
  adjacent is defined as the existence of an edge originating
  from x and terminating at y."
  [graph x y]
  (contains? (:edges graph) [x y]))

(defn adj-edges
  "Returns the set of all edges that v is adjacent to, meaning
  all edges that originate from v."
  [graph v]
  (into #{} (filter #(= v (first %)) (:edges graph))))

(defn indegree
  "Returns the indegree of the given vertex, where indegree
  is defined as the number of edges that terminate at a given
  vertex. Returns nil if the given vertex is not present in the
  graph."
  [graph v]
  (when ((:vertices graph) v)
    (->> (:edges graph)
         (filter #(= (second %) v))
         count)))

(defn outdegree
  "Returns the outdegree of the given vertex, where outdegree
  is defined as the number of edges that originate from a given
  vertex. Returns nil if the given vertex is not present in the
  graph."
  [graph v]
  (when ((:vertices graph) v)
    (->> (:edges graph)
         (filter #(= (first %) v))
         count)))

(defn source?
  "Returns true if the given vertex is a source, where source
  is defined as having an indegree count of zero, and a non-zero
  outdegree count."
  [graph v]
  (and (zero? (indegree graph v))
       (pos? (outdegree graph v))))

(defn sink?
  "Returns true if the given vertex is a sink, where sink
  is defined as having an outdegree count of zero, and a
  non-zero indegree count."
  [graph v]
  (and (zero? (outdegree graph v))
       (pos? (indegree graph v))))

(defn internal?
  "Returns true if the given vertex is internal, where
  internal is defined as having a non-zero indegree and outdegree."
  [graph v]
  (and (pos? (indegree graph v))
       (pos? (outdegree graph v))))

(defn stranded?
  "Returns true if the given vertex is stranded, meaning that
  both its indegree and outdegree are zero."
  [graph v]
  (and (zero? (indegree graph v))
       (zero? (outdegree graph v))))

(defn topological-sort
  "Returns a vector of the given graph's vertices in topological order,
  or nil if the graph can not be sorted (e.g. it contains a cycle).
  Uses Kahn's algorithm to compute the sort."
  [{:keys [vertices] :as graph}]
  (loop [l []
         q (into clojure.lang.PersistentQueue/EMPTY
                 (filter #(zero? (indegree graph %)) vertices))
         g graph]
    (if (empty? q)
      (when (empty? (:edges g)) l)
      (let [n (peek q)
            [new-q new-g] (reduce (fn [[q g] e]
                                    (let [[_ m] e
                                          g (update g :edges disj e)
                                          m-in (indegree g m)]
                                      [(if (zero? m-in) (conj q m) q) g]))
                                  [q g]
                                  (adj-edges g n))]
        (recur (conj l n) (pop new-q) new-g)))))

(defn cyclic?
  "Returns true if the given graph contains a cycle, false otherwise."
  [graph]
  (nil? (topological-sort graph)))

;""""""""""""""""""""""""""""""""""""""""
;
; Specifications
;
; DIGRAPH is a map containing a set of EDGE and a set of VERTEX
; EDGE is a tuple of two VERTEX
; VERTEX is a positive integer
;
;""""""""""""""""""""""""""""""""""""""""

(s/def ::vertex pos-int?)
(s/def ::vertices (s/coll-of ::vertex :kind set? :into #{}))
(s/def ::edge (s/tuple ::vertex ::vertex))
(s/def ::edges (s/coll-of ::edge :kind set? into #{}))
(s/def ::digraph-type (s/and (s/keys :req-un [::vertices ::edges])
                             #(cset/subset? (reduce (fn [a x] (into a x)) #{} (:edges %))
                                            (:vertices %))))

(defn digraph-gen
  "Generator for digraphs."
  []
  (gen/bind (gen/such-that not-empty (s/gen ::vertices))
            #(s/gen ::digraph-type {::vertex (fn [] (gen/elements %))})))

(s/def ::digraph (s/with-gen ::digraph-type digraph-gen))
(s/def ::dag (s/and ::digraph #(not (cyclic? %))))

;""""""""""""""""""""""""""""""""""""""""
; Function specs
;""""""""""""""""""""""""""""""""""""""""
(s/fdef adj?
        :args (s/cat :g ::digraph :x ::vertex :y ::vertex)
        :ret boolean?)

(s/fdef adj-edges
        :args (s/cat :g ::digraph :v ::vertex)
        :ret ::edges
        :fn #(= (outdegree (-> % :args :g) (-> % :args :v))
                (count (:ret %))))

(s/fdef indegree
        :args (s/cat :g ::digraph :v ::vertex)
        :ret (s/nilable nat-int?))

(s/fdef outdegree
        :args (s/cat :g ::digraph :v ::vertex)
        :ret (s/nilable nat-int?))

(s/fdef source?
        :args (s/cat :g ::digraph :v ::vertex)
        :ret boolean?)

(s/fdef sink?
        :args (s/cat :g ::digraph :v ::vertex)
        :ret boolean?)

(s/fdef internal?
        :args (s/cat :g ::digraph :v ::vertex)
        :ret boolean?)

(s/fdef stranded?
        :args (s/cat :g ::digraph :v ::vertex)
        :ret boolean?)

(s/fdef topological-sort
        :args (s/cat :g ::digraph)
        :ret (s/nilable (s/coll-of ::vertex :kind vector?))
        :fn (fn [%] (or (nil? (:ret %))
                        (and (= (count (-> % :args :g :vertices))
                                (count (:ret %)))
                             (every? true? (for [[u v] (-> % :args :g :edges)]
                                             (< (.indexOf (:ret %) u)
                                                (.indexOf (:ret %) v))))))))

(s/fdef cyclic?
        :args (s/cat :g ::digraph)
        :ret boolean?)
