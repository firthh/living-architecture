(ns living-architecture.config
  (:require [cheshire.core :as json]
            [clojure.java.io :as io]))

(defn application-config []
  (json/parse-string (slurp (io/file (io/resource "config.json"))) true))
