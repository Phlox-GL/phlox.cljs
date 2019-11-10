
(ns phlox.core
  (:require ["pixi.js" :as PIXI]
            [phlox.render :refer [render-element]]
            [phlox.util :refer [hslx]]))

(defonce *app (atom nil))

(defonce *events-element (atom nil))

(defonce *renderer (atom nil))

(defonce *tree-element (atom nil))

(defn mount-app! [app]
  (js/console.log "mount" app)
  (let [element-tree (render-element app)]
    (.addChild (.-stage @*app) element-tree)
    (js/console.log "got tree" element-tree)))

(defn rerender-app! [app] (js/console.log "rerender tree" app))

(defn render! [app]
  (when (nil? @*app)
    (let [pixi-app (PIXI/Application.
                    (clj->js
                     {:background-color (hslx 0 0 30),
                      :antialias true,
                      :width js/window.innerWidth,
                      :height js/window.innerHeight,
                      :interactive true}))]
      (reset! *app pixi-app)
      (-> js/document .-body (.appendChild (.-view pixi-app)))))
  (if (nil? @*tree-element) (mount-app! app) (rerender-app! app))
  (reset! *tree-element app))

(defn render-tag [tag props & children]
  {:tag tag, :phlox-node :element, :props props, :children children})
