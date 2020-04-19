
(ns phlox.input
  (:require [respo.render.html :refer [style->string]]
            [hsl.core :refer [hsl]]
            [respo-ui.core :as ui]
            [lilac.core
             :refer
             [record+
              number+
              string+
              optional+
              tuple+
              enum+
              map+
              fn+
              any+
              keyword+
              boolean+
              vector+
              or+
              is+]]
            [phlox.check :refer [dev-check]]))

(def lilac-input
  (record+
   {:placeholder (string+),
    :initial (string+),
    :style (map+ (keyword+) (any+)),
    :textarea? (boolean+)}
   {:all-optional? true, :check-keys? true}))

(defn request-text! [e options cb]
  (dev-check options lilac-input)
  (let [root (js/document.createElement "div")
        textarea? (:textarea? options)
        input (js/document.createElement (if textarea? "textarea" "input"))
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
        :width 240,
        :border-radius "2px"}
       (if textarea? {:width 320})
       (if (< (- js/window.innerWidth x) 240) {:left nil, :right 8})
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
        :height 28}
       (if textarea? {:height 80})
       (:style options))))
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
    (set! (.-value input) (or (:initial options) ""))
    (set! (.-innerText close) "×")
    (.addEventListener
     input
     "keydown"
     (fn [event]
       (when (and (= "Enter" (.-key event)) (if textarea? (.-metaKey event) true))
         (cb (.-value input))
         (.remove root))))
    (.addEventListener close "click" (fn [event] (.remove root)))
    (.appendChild js/document.body root)
    (.select input)))
