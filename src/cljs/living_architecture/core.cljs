(ns living-architecture.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [living-architecture.events :as events]
   [living-architecture.event-listeners :refer [init-listeners!]]
   [living-architecture.subs :refer [init-subs!]]
   [living-architecture.views :as views]
   [living-architecture.config :as config]
   ))


(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (init-listeners!)
  (init-subs!)
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root))
