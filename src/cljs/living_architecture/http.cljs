(ns living-architecture.http
  (:require [ajax-promises.core :as http]
            [promesa.core :as p]
            [re-frame.core :as re-frame]
            [living-architecture.events :as events]))

(defn dispatch [response]
  (re-frame/dispatch [::events/new-diagram response]))

(defn error-handler [error]
  (.log js/console error))

(defn parse-json [string]
  (js->clj (.parse js/JSON string)
           :keywordize-keys true))

(defn resolve-promise [response]
  (-> response
      (p/then :body)
      (p/then parse-json)
      (p/then dispatch)
      (p/catch error-handler)))

(defn get-diagram [diagram-id]
  ;; we don't do anything with diagram id yet
  ;; as we don't support multiple diagrams yet
  (-> (http/GET {:uri "/diagram"})
      resolve-promise))

(defn get-metric-values [diagram-id]
  ;; we don't do anything with diagram id yet
  ;; as we don't support multiple diagrams yet
  (-> (http/GET {:uri "/metric-values"})
      resolve-promise))
