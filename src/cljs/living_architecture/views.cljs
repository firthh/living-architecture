(ns living-architecture.views
  (:require [re-frame.core :as re-frame]
            [living-architecture.subs :as subs]
            [living-architecture.events :as events]
            [living-architecture.view.components :as components]))

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])
        diagram (re-frame/subscribe [::subs/diagram])
        diagram-id "123"]
    [:div
     [:h1 "Hello from " @name]
     [:div
      [:button {:on-click #(re-frame/dispatch [::events/get-diagram diagram-id])} "Get diagram"]
      [:button {:on-click #(re-frame/dispatch [::events/update-metric-values diagram-id])} "Update metric values"]]
     [:br]
     [components/diagram-view @diagram]]))
