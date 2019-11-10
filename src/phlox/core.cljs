
(ns phlox.core (:require ["pixi.js" :as PIXI] [phlox.render :refer [render-element]]))

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
    (let [pixi-app (PIXI/Application. 800 400 (clj->js {:background-color 0x000000}))]
      (reset! *app pixi-app)
      (-> js/document .-body (.appendChild (.-view pixi-app)))))
  (if (nil? @*tree-element) (mount-app! app) (rerender-app! app))
  (reset! *tree-element app))

(defn render-tag [tag props & children] {:tag tag, :props props, :children children})
