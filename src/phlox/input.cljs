
(ns phlox.input
  (:require [respo.render.html :refer [style->string]]
            [hsl.core :refer [hsl]]
            [respo-ui.core :as ui]))

(defn request-text! [e options cb]
  (let [root (js/document.createElement "div")
        input (js/document.createElement "input")
        x (-> e .-data .-global .-x)
        y (-> e .-data .-global .-y)
        close (js/document.createElement "span")]
    (.appendChild root input)
    (.appendChild root close)
    (set!
     (.-style root)
     (style->string
      (merge
       ui/row-middle
       {:position :absolute,
        :top y,
        :left x,
        :padding "10px 12px",
        :background-color (hsl 0 0 30 0.9),
        :border (str "1px solid " (hsl 0 0 30)),
        :width 200,
        :border-radius "2px"}
       (if (< (- js/window.innerWidth x) 200) {:left nil, :right 8})
       (if (< (- js/window.innerHeight y) 70) {:top nil, :bottom 8}))))
    (set!
     (.-style input)
     (style->string
      (merge
       ui/expand
       {:outline :none,
        :font-family ui/font-normal,
        :line-height "20px",
        :font-size 14,
        :padding "6px 8px",
        :width "100%",
        :border-radius "2px",
        :border :none,
        :height 28})))
    (set!
     (.-style close)
     (style->string
      {:margin-left 8,
       :font-family "Helvetica, sans-serif",
       :font-size 24,
       :font-weight 100,
       :color (hsl 0 80 80),
       :cursor :pointer}))
    (set! (.-placeholder input) (or (:placeholder options) "text..."))
    (set! (.-value input) (or (:inital options) ""))
    (set! (.-innerText close) "×")
    (.addEventListener
     input
     "keydown"
     (fn [event] (when (= "Enter" (.-key event)) (cb (.-value input)) (.remove root))))
    (.addEventListener close "click" (fn [event] (.remove root)))
    (.appendChild js/document.body root)
    (.focus input)))
