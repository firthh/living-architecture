(ns living-architecture.http
  (:require [ajax.core :refer [GET]]
            [re-frame.core :as re-frame]
            [living-architecture.events :as events]))

(defn handler [response]
  (re-frame/dispatch [::events/new-diagram response]))

(defn error-handler [{:keys [status status-text]}]
  (.log js/console (str "something bad happened: " status " " status-text)))

(defn get-diagram [diagram-id]
  ;; we don't do anything with diagram id yet
  ;; as we don't support multiple diagrams yet
  (GET "/diagram" {:handler handler
                   :response-format :json
                   :keywords? true
                   :error-handler error-handler}))

(defn get-metric-values [diagram-id]
  ;; we don't do anything with diagram id yet
  ;; as we don't support multiple diagrams yet
  (GET "/metric-values" {:handler handler
                         :response-format :json
                         :keywords? true
                         :error-handler error-handler}))
