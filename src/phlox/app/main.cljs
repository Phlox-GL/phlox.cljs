
(ns phlox.app.main (:require ["pixi.js" :as PIXI]))

(defn main! []
  (println "App Started")
  (js/console.log PIXI)
  (let [app (PIXI/Application. 800 600 (clj->js {:background-color 0x1099bb}))
        g (PIXI/Graphics.)]
    (.appendChild js/document.body (.-view app))
    (-> g
        (.beginFill 0xff3300)
        (.lineStyle 4 0xffd900 1)
        (.moveTo 50 50)
        (.lineTo 100 100)
        (.endFill))
    (.addChild (.-stage app) g)))

(defn reload! [] (println "Code updated"))
