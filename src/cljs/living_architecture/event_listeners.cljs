(ns living-architecture.event-listeners
  (:require [re-frame.core :as re-frame]
            [living-architecture.db :as db]
            [living-architecture.http :as http]
            [living-architecture.events :as events]
            [day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]
            ))

(defn init-listeners! []
  (re-frame/reg-event-db
    ::events/initialize-db
    (fn-traced [_ _]
               db/default-db))

  (re-frame/reg-event-db
   ::events/new-diagram
   (fn-traced [db [_ response]]
              (merge db response)))

  (re-frame/reg-event-db
   ::events/get-diagram
   (fn-traced [db [_ diagram-id]]
              (http/get-diagram diagram-id)
              db))

  (re-frame/reg-event-db
   ::events/update-metric-values
   (fn-traced [db [_ diagram-id]]
              (http/get-metric-values diagram-id)
              db))

  (re-frame/reg-event-db
   ::events/new-metric-values
   (fn-traced [db [_ response]]
              (merge db response))))
