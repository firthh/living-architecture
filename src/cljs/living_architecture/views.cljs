(ns living-architecture.views
  (:require
   [re-frame.core :as re-frame]
   [living-architecture.subs :as subs]
   [living-architecture.events :as events]
   [living-architecture.http :as http]
   [clojure.string]))

(def not-blank?
  (comp not clojure.string/blank?))

(def colours ["black" "black" "green" "#FFD700" "red"])

(defn metric-component [{:keys [position id]}]
  (let [{:keys [name value unit]} @(re-frame/subscribe [::subs/metric-value id])]
    [:tspan (merge {:fill (rand-nth colours)}
                   position)
     (when (not-blank? name) (str name " - "))
     value
     (when (not-blank? unit) (str " " unit))]))

(def line-size 20)

(defn database-component [{:keys [id name metrics] {:keys [x y]} :position}]
  (let [pad-top 30
        pad-left "50%"]
    [:svg {:x x :y y :width 202 :height 400
           :style {:outline "0px solid black"
                   :background-color "#fff"}}
     [:path {:fill "none" :stroke "black" :stroke-width 2
             :d "M1 30
               C 0 0, 200 0, 200 30
               C 200 60, 0 60, 1 30
               V 150
               C 1 180, 200 180, 200 150
               V 30"}]
     [:text {:x pad-left :y 0
             :text-anchor "middle"
             :style {"fontWeight" "bold"}}
      [:tspan {:y pad-top
               :style {"fontSize" "1.5em"}} name]
      (map-indexed (fn [idx m]
                     ^{:key (str "database-" id "-" (:id m))}
                     [metric-component (assoc m :position {:x pad-left :y (+ (* 3 pad-top) (* line-size idx))})])
                   metrics)]]))

(defn box-component [{:keys [id name metrics] {:keys [x y]} :position}]
  (let [pad-top 20
        pad-left "50%"]
    [:svg {:x x :y y
           :width 200 :height 150}
     [:rect {:x 0 :y 0
             :height "100%" :width "100%"
             :style {:fill "#F0FFFF"
                     :stroke-width 3
                     :stroke "black"}}]
     [:text {:x pad-left :y 0
             :text-anchor "middle"
             :style {"fontWeight" "bold"}}
      [:tspan {:y pad-top
               :style {"fontSize" "1.5em"}} name]
      (map-indexed (fn [idx m]
                     ^{:key (str "box-" id "-" (:id m))}
                     [metric-component (assoc m :position {:x pad-left :y (+ (* 3 pad-top) (* line-size idx))})])
                   metrics)]]))

(defn arrow-component [{:keys [position]}]
  [:line (merge {:stroke "black"
                 :stroke-width 2}
                position)])

(defn individual-component [{:keys [type id] :as c}]
  (condp = type
    "box"      [box-component c]
    "database" [database-component c]
    "arrow"    [arrow-component c]
    ))

(defn diagram-components [diagram]
  [:svg {:width "1000"
         :height "2000"
         :id "canvas"
         :style {:outline "2px solid black"
                 :background-color "#fff"}}
   (for [c diagram]
     ^{:key (str "component-" (:id c))}
     [individual-component c])])

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
