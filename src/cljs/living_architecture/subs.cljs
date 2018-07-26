(ns living-architecture.subs
  (:require
   [re-frame.core :as re-frame]))

(defn init-subs! []
  (re-frame/reg-sub
   ::name
   (fn [db]
     (:name db)))

  (re-frame/reg-sub
   ::diagram
   (fn [db]
     (:diagram db)))

  (re-frame/reg-sub
   ::metric-value
   (fn [db [_ metric-id]]
     (get-in db [:metrics (keyword metric-id)]))))

