(ns living-architecture.prometheus
  (:require [org.httpkit.client :as http]
            [living-architecture.config :refer [application-config]]
            [cemerick.url :as url]
            [clojure.walk :refer [postwalk]]
            [camel-snake-kebab.core :refer [->kebab-case-keyword]]
            [clojure.data.json :as json])
  (:import java.math.RoundingMode))

(defn kebabify
  "Recursively transforms all map keys from strings to keywords."
  {:added "1.1"}
  [m]
  (let [f (fn [[k v]] (if (string? k) [(->kebab-case-keyword k) v] [k v]))]
    ;; only apply to maps
    (postwalk (fn [x] (if (map? x) (into {} (map f x)) x)) m)))

(def root "http://demo.robustperception.io:9090/api/v1")

(defn prepare-response [body]
  (kebabify body))

(defn get-path [path]
  (let [{:keys [body] :as all} (-> root
                                   (str path)
                                   http/get
                                   deref)]
   (-> body
       json/read-str
       prepare-response)))

(defn not-prometheus [s]
  (not (re-find #"^prometheus_" s)))

(defn targets []
  (get-path "/targets"))

(defn metric-names []
  (->> "/label/__name__/values"
      get-path
      :data
      (filter not-prometheus)))

(defn get-metric [query]
  (let [response (->> query
                      url/url-encode
                      (str "/query?query=")
                      get-path)]
    (some-> response
            (get-in [:data :result 0 :value 1])
            bigdec
            (.setScale 2 RoundingMode/HALF_EVEN))))

;; (get-metric "avg(avg without (quantile)(rate(http_request_duration_microseconds[5m]) >= 0))")

(defn query-metrics []
  (let [metrics (:metric (application-config))]
    (->> metrics
         (map (fn [[k v]]
                [k (-> v
                       (assoc :value (get-metric (:query v)))
                       (select-keys [:name :value :unit]))]))
         (into {}))))

