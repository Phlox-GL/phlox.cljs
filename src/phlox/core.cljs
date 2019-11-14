
(ns phlox.core
  (:require ["pixi.js" :as PIXI]
            [phlox.render :refer [render-element update-element update-children]]
            [phlox.util :refer [hslx]]))

(defonce *app (atom nil))

(defonce *events-element (atom nil))

(defonce *renderer (atom nil))

(defonce *tree-element (atom nil))

(defn mount-app! [app dispatch!]
  (js/console.log "mount" app)
  (let [element-tree (render-element app dispatch!)]
    (.addChild (.-stage @*app) element-tree)
    (js/console.log "got tree" element-tree)))

(defn rerender-app! [app dispath!]
  (js/console.log "rerender tree" app)
  (update-children (list [0 app]) (list [0 @*tree-element]) (.-stage @*app) dispath!))

(defn render! [app dispatch!]
  (when (nil? @*app)
    (let [pixi-app (PIXI/Application.
                    (clj->js
                     {:background-color (hslx 0 0 30),
                      :antialias true,
                      :width js/window.innerWidth,
                      :height js/window.innerHeight,
                      :interactive true}))]
      (reset! *app pixi-app)
      (-> js/document .-body (.appendChild (.-view pixi-app))))
    (set! js/window._phloxTree @*app))
  (if (nil? @*tree-element) (mount-app! app dispatch!) (rerender-app! app dispatch!))
  (reset! *tree-element app))

(defn render-tag [tag props & children]
  {:name tag, :phlox-node :element, :props props, :children children})
