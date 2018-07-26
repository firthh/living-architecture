(ns living-architecture.handler
  (:require [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [resources]]
            [ring.util.response :refer [resource-response]]
            [ring.middleware.reload :refer [wrap-reload]]
            [cheshire.core :as json]
            [clojure.java.io :as io]))

(def metric-configuration
  {"met1" {:source :prometheus
           :host ""
           :query ""}
   "met2" {:source :prometheus
           :host ""
           :query ""}
   "met3" {:source :prometheus
           :host ""
           :query ""}
   "met4" {:source :prometheus
           :host ""
           :query ""}})

(def application-config
  (delay (json/parse-string (slurp (io/file (io/resource "config.json"))) true)))

(defroutes routes
  (GET "/" [] (resource-response "index.html" {:root "public"}))
  (GET "/diagram" [] {:status 200
                      :body (json/generate-string {:diagram (:diagram @application-config)})})
  (GET "/metric-values" [] {:status 200
                            :body (json/generate-string {:metrics {"met1" {:name "" :value 50 :unit "rps"}
                                                                   "met2" {:name "latency" :value 100 :unit "ms"}
                                                                   "met3" {:name "" :value 30 :unit "rps"}
                                                                   "met4" {:name "latency" :value 150 :unit "ms"}}})})
  (resources "/"))

(def dev-handler (-> #'routes wrap-reload))

(def handler routes)
