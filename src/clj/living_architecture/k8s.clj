(ns living-architecture.k8s
  (:require [org.httpkit.client :as http]
            [clojure.data.json :as json]
            [clojure.walk :refer [postwalk]]
            [camel-snake-kebab.core :refer [->kebab-case-keyword]]))

(def root (delay (System/getenv "K8S_ROOT")))

(defn clean-up-pod [pod]
  {:name (get-in pod [:metadata :name])
   :start-time (get-in pod [:status :start-time])
   :status (get-in pod [:status :phase])})

(defn kebabify
  "Recursively transforms all map keys from strings to keywords."
  {:added "1.1"}
  [m]
  (let [f (fn [[k v]] (if (string? k) [(->kebab-case-keyword k) v] [k v]))]
    ;; only apply to maps
    (postwalk (fn [x] (if (map? x) (into {} (map f x)) x)) m)))

(defn get-pods []
  (let [{body :body} @(http/get (str @root "/api/v1/pods"))
        pods (-> body
                     json/read-str
                     kebabify
                     :items)]
    (map clean-up-pod pods)))
