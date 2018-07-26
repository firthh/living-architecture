(ns living-architecture.handler
  (:require [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [resources]]
            [ring.util.response :refer [resource-response]]
            [ring.middleware.reload :refer [wrap-reload]]
            [cheshire.core :as json]
            [clojure.java.io :as io]
            [living-architecture.prometheus :as p]
            [living-architecture.config :refer [application-config]]))

(defroutes routes
  (GET "/" [] (resource-response "index.html" {:root "public"}))
  (GET "/diagram" [] {:status 200
                      :body (json/generate-string {:diagram (:diagram (application-config))})})
  (GET "/metric-values" [] {:status 200
                            :body (json/generate-string {:metrics (p/query-metrics)})})
  (resources "/"))

(def dev-handler (-> #'routes wrap-reload))

(def handler routes)
