
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

(def style-close
  {:margin-left 8,
   :font-family "Helvetica, sans-serif",
   :font-size 24,
   :font-weight 100,
   :color (hsl 0 80 80),
   :cursor :pointer})

(def style-container
  {:position :absolute,
   :padding "10px 12px",
   :background-color (hsl 0 0 30 0.9),
   :border (str "1px solid " (hsl 0 0 30)),
   :width 240,
   :border-radius "2px"})

(def style-input
  {:outline :none,
   :font-family ui/font-normal,
   :line-height "20px",
   :font-size 14,
   :padding "6px 8px",
   :width "100%",
   :border-radius "2px",
   :border :none,
   :height 28})

(def style-submit {:margin-left 8, :color (hsl 200 80 80), :cursor :pointer})

(defn request-text! [e options cb]
  (dev-check options lilac-input)
  (let [root (js/document.createElement "div")
        control (js/document.createElement "div")
        textarea? (:textarea? options)
        input (js/document.createElement (if textarea? "textarea" "input"))
        submit (js/document.createElement "a")
        x (-> e .-data .-global .-x)
        y (-> e .-data .-global .-y)
        close (js/document.createElement "span")]
    (.appendChild root input)
    (.appendChild root control)
    (.appendChild control close)
    (when textarea?
      (.appendChild control submit)
      (set! (.-innerText submit) "Ok")
      (.appendChild root control))
    (set!
     (.-style root)
     (style->string
      (merge
       ui/row
       style-container
       {:top y, :left x}
       (if textarea? {:width 320})
       (if (< (- js/window.innerWidth x) 240) {:left nil, :right 8})
       (if (< (- js/window.innerHeight y) 70) {:top nil, :bottom 8}))))
    (set!
     (.-style input)
     (style->string
      (merge ui/expand style-input (if textarea? {:height 80}) (:style options))))
    (set!
     (.-style control)
     (style->string (merge ui/column {:justify-content :space-evenly})))
    (set! (.-style close) (style->string style-close))
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
    (when textarea?
      (set! (.-style submit) (style->string style-submit))
      (.addEventListener submit "click" (fn [event] (cb (.-value input)) (.remove root))))
    (.appendChild js/document.body root)
    (.select input)))
