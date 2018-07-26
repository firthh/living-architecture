(ns living-architecture.handler
  (:require [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [resources]]
            [ring.util.response :refer [resource-response]]
            [ring.middleware.reload :refer [wrap-reload]]
            [cheshire.core :as json]))

(defroutes routes
  (GET "/" [] (resource-response "index.html" {:root "public"}))
  (GET "/diagram" [] {:status 200
                      :body (json/generate-string {:diagram [{:name "Load Balancer 1"
                                                              :id "lb1"
                                                              :position {:x 100 :y 100}
                                                              :metrics [{:id "met1" :name "rps" :value 50}
                                                                        {:id "met2" :name "latency" :value 100}]}
                                                             {:name "Load Balancer 2"
                                                              :id "lb2"
                                                              :position {:x 500 :y 100}
                                                              :metrics [{:id "met3" :name "rps" :value 30}
                                                                        {:id "met4" :name "latency" :value 150}]}]})})
  (GET "/metric-values" [] {:status 200
                            :body (json/generate-string {:metrics {"met1" {:name "" :value 50 :unit "rps"}
                                                                   "met2" {:name "latency" :value 100 :unit "ms"}
                                                                   "met3" {:name "" :value 30 :unit "rps"}
                                                                   "met4" {:name "latency" :value 150 :unit "ms"}}})})
  (resources "/"))

(def dev-handler (-> #'routes wrap-reload))

(def handler routes)
