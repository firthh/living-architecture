(ns living-architecture.views
  (:require
   [re-frame.core :as re-frame]
   [living-architecture.subs :as subs]
   [living-architecture.events :as events]
   [living-architecture.http :as http]
   [clojure.string]))

(def not-blank?
  (comp not clojure.string/blank?))

(defn metric-component [{:keys [position id]}]
  (let [{:keys [name value unit]} @(re-frame/subscribe [::subs/metric-value id])]
    [:tspan position
     (when (not-blank? name) (str name " - "))
     value
     (when (not-blank? unit) (str " " unit))]))

(def line-size 20)

(defn box-component [{:keys [name metrics] {:keys [x y]} :position}]
  (let [pad-top 20
        pad-left "50%"]
    [:svg {:x x :y y
           :width 200 :height 100}
     [:rect {:x 0 :y 0
             :height "100%" :width "100%"
             :style {:fill "white"
                     :stroke-width 1
                     :stroke "black"}}]
     [:text {:x pad-left :y 0
             :text-anchor "middle"}
      [:tspan {:y pad-top
               :style {"fontWeight" "bold"}} name]
      (map-indexed (fn [idx m]
                     ^{:key (str "component-" name "-" (:name m))}
                     [metric-component (assoc m :position {:x pad-left :y (+ (* 3 pad-top) (* line-size idx))})])
                   metrics)]]))

(defn diagram-components [diagram]
  [:svg {:width "1000"
         :height "2000"
         :id "canvas"
         :style {:outline "2px solid black"
                 :background-color "#fff"}}
   (for [c diagram]
     ^{:key (str "component-" (:id c))}
     [box-component c])])

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
     [diagram-components @diagram]]))
