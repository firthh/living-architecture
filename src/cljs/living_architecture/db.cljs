(ns living-architecture.db)

(def default-db
  {:name "re-frame"
   :diagram [{:name "Load Balancer 1"
              :id "lb1"
              :position {:x 100 :y 100}
              :metrics [{:name "rps" :value 50}
                        {:name "latency" :value 100}]}
             {:name "Load Balancer 2"
              :id "lb2"
              :position {:x 500 :y 100}
              :metrics [{:name "rps" :value 30}
                        {:name "latency" :value 150}]}]})
