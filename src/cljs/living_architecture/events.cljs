(ns living-architecture.events
  (:require [re-frame.core :as re-frame]
            [living-architecture.db :as db]
            [living-architecture.http :as http]
            [day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]
            ))

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
            db/default-db))

;; Can't use namespaced keywords here as
;; we get cyclic dependencies between this and the http namespace
(re-frame/reg-event-db
 :new-diagram
 (fn-traced [db [_ response]]
            (merge db response)))

(re-frame/reg-event-db
 ::get-diagram
 (fn-traced [db [_ diagram-id]]
            (http/get-diagram diagram-id)
            db))
