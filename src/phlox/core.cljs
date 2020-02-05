
(ns phlox.core
  (:require ["pixi.js" :as PIXI]
            [phlox.render :refer [render-element update-element update-children]]
            [phlox.util :refer [hslx index-items remove-nil-values]]
            [phlox.render.expand :refer [expand-tree]])
  (:require-macros [phlox.core]))

(defonce *app (atom nil))

(defonce *events-element (atom nil))

(defonce *renderer (atom nil))

(defonce *tree-element (atom nil))

(defn create-element [tag props children]
  {:name tag,
   :phlox-node :element,
   :props props,
   :children (remove-nil-values (index-items children))})

(defn create-list [tag props children]
  {:name tag, :phlox-node :element, :props props, :children (remove-nil-values children)})

(defn mount-app! [app dispatch!]
  (js/console.log "mount" app)
  (let [element-tree (render-element app dispatch!)]
    (.addChild (.-stage @*app) element-tree)
    (js/console.log "got tree" element-tree)))

(defn rerender-app! [app dispatch! options]
  (comment js/console.log "rerender tree" app @*tree-element)
  (update-children
   (list [0 app])
   (list [0 @*tree-element])
   (.-stage @*app)
   dispatch!
   options))

(defn render! [app dispatch! options]
  (when (nil? @*app)
    (let [pixi-app (PIXI/Application.
                    (clj->js
                     {:backgroundColor (hslx 0 0 0),
                      :antialias true,
                      :width js/window.innerWidth,
                      :height js/window.innerHeight,
                      :interactive true}))]
      (reset! *app pixi-app)
      (-> js/document .-body (.appendChild (.-view pixi-app)))
      (.addEventListener
       js/window
       "resize"
       (fn [] (-> pixi-app .-renderer (.resize js/window.innerWidth js/window.innerHeight)))))
    (set! js/window._phloxTree @*app))
  (let [expanded-app (expand-tree app)]
    (comment js/console.log "render!" expanded-app)
    (if (nil? @*tree-element)
      (mount-app! expanded-app dispatch!)
      (rerender-app! expanded-app dispatch! options))
    (reset! *tree-element expanded-app)))
