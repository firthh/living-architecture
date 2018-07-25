(ns living-architecture.http
  (:require [ajax.core :refer [GET]]
            [re-frame.core :as re-frame]
            [living-architecture.events :as events]))

(defn handler [response]
  (re-frame/dispatch [::events/new-diagram response]))

(defn error-handler [{:keys [status status-text]}]
  (.log js/console (str "something bad happened: " status " " status-text)))

(defn get-diagram [diagram-id]
  (GET "/diagram" {:handler handler
                   :response-format :json
                   :keywords? true
                   :error-handler error-handler}))
