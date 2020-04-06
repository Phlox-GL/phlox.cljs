
Phlox
----

> Pixi.js DSL in ClojureScript with hot code swapping, inspired by Virtual DOMs. Currently only a small subset of Pixi.js features is supported.

Previews http://repo.quamolit.org/phlox/ .

### Usage

[![Clojars Project](https://img.shields.io/clojars/v/quamolit/phlox.svg)](https://clojars.org/quamolit/phlox)

```edn
[quamolit/phlox "0.2.0"]
```

`render!` to add canvas to `<body/>`:

```clojure
(ns app.main
  (:require [phlox.core :refer [defcomp hslx render! create-list
                                rect circle text container graphics >>]]))

(defcomp comp-demo [data]
  (rect
   {:position [800 40],
    :size [60 34],
    :fill (hslx 40 80 80),
    :on {:pointerdown (fn [e d!] (d! :demo nil))}}
   (text
    {:text "Demo",
     :position [808 44],
     :style {:fill (hslx 120 80 20), :font-size 18, :font-family "Josefin Sans"}})))

(defonce *store (atom nil))

(defn dispatch! [op op-data]
  (reset! *store (updater @*store op op-data)))

(defn main []
  (render! (comp-demo data) dispatch! {}))

(defn reload! []
  (render! (comp-container @*store) dispatch! {:swap? true}))
```

### Global keyboard events

Phlox supports a naive global event system for listening to keyboard events from elements:

```clojure
:on-keyboard {
  :down (fn [e dispatch!])
  :press (fn [e dispatch!])
  :up (fn [e dispatch!])
}
```

### Spec

Add a container:

```edn
{
  :position [1 1]
  :pivot [0 0]
  :rotation 0
  :alpha 1
  :on {
    :pointerdown (fn [])
  }
  :on-keyboard {
    :down (fn [])
  }
}
```

Draw a circle:

```edn
{
  :position [1 1]
  :radius 1
  :line-style {
    :width 2
    :color 0x000001
    :alpha 1
  }
  :fill 0x000001
  :on {
    :pointerdown (fn [])
  }
  :alpha 1
  :on-keyboard {
    :down (fn [])
  }
}
```

Draw a rectangle:

```edn
{
  :position [1 2]
  :size [1 1]
  :line-style {
    :width 2
    :color 0x000001
    :alpha 1
  }
  :fill 0x000001
  :on {
    :pointerdown (fn [])
  }
  :radius 1
  :rotation 1
  :pivot [1 2]
  :alpha 1
  :on-keyboard {
    :down (fn [])
  }
}
```

Draw text:

```edn
{
  :text "demo"
  :position [1 1]
  :pivot [0 0]
  :rotation 0
  :alpha 1
  :style {
    :fill "red"
    :font-size 14
    :font-family "Hind"
  }
  :on-keyboard {
    :down (fn [])
  }
}
```

Draw graphics(use `phlox.core/g` for validations):

```edn
{
  :ops [
    (g :move-to [1 1])
    (g :line-to [2 2])
    (g :line-style {})
    (g :begin-fill {:color "red"})
    (g :end-fill nil)
    (g :close-path nil)
    (g :arc-to {:p1 [200 200], :p2 [240 180], :radius 90})
    (g :arc {:center [260 120], :radius 40, :angle [70 60], :anticlockwise? false})
    (g :bezier-to {:p1 [400 500], :p2 [300 200], :to-p [600 300]})
    (g :quadratic-to {:p1 [400 100], :to-p [500 400]})
  ]
  :position [1 1]
  :pivot [1 2]
  :rotation 0
  :alpha 1
  :on {
    :pointerdown (fn [])
  }
  :on-keyboard {
    :down (fn [])
  }
}
```

Notice that Pixi.js takes colors in hex numbers. `phlox.core/hslx` is added for convenience.

### Components

`phlox.comp.button/comp-button` provides a clickable button:

```clojure
(comp-button
 {:text "DEMO BUTTON",
  :position [100 0],
  :on {:click (fn [e d!] (js/console.log "clicked" e d!))}})

(comp-button
 {:text "Blue", :position [100 60], :color (hslx 0 80 70), :fill (hslx 200 80 40)}))

(comp-button
 {:text "Quick click",
  :position [100 0],
  :on-click (fn [e d!] (js/console.log "clicked" e d!))})
```

`phlox.comp.slider/comp-slider` provides a little slider bar of a number, changes on dragging:

```clojure
(comp-slider (>> states :c)
 {:value (:c state),
  :unit 10,
  :min 1
  :max 10
  :round? true
  :position [20 120],
  :fill (hslx 50 90 70),
  :color (hslx 200 90 30),
  :on-change (fn [value d!] (d! cursor (assoc state :c value)))})
```

`phlox.comp.drag-point/comp-dragging-point` provides a point for dragging:

```clojure
(comp-drag-point (>> states :p3)
 {:position (:p3 state),
  :unit 0.4,
  :title "DEMO"
  :radius 6,
  :fill (hslx 0 90 60),
  :color (hslx 0 0 50),
  :hide-text? false
  :on-change (fn [position d!] (d! cursor (assoc state :p3 position)))})
```

`phlox.comp.switch/comp-switch` provides a switch button:

```clojure
(comp-switch
 {:value (:value state),
  :position [100 20],
  :title "Custom title",
  :on-change (fn [value d!] (d! cursor (assoc state :value value)))})
```

### Cursor and states

`>>` for branching states:

```clojure
(phlox.core/>> state :a)
```

`update-states` for handling states change, used in updater:

```clojure
(phlox.cursor/update-states store [cursor op-data])
```

### Workflow

Workflow https://github.com/Quamolit/phlox-workflow

### License

MIT
