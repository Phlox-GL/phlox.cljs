
Phlox
----

> Pixi.js DSL in ClojureScript, inspired by Virtual DOMs. Currently only a small subset of Pixi.js features is supported, good part is how swapping on code changes is available.

Previews http://repo.quamolit.org/phlox/ .

### Usage

[![Clojars Project](https://img.shields.io/clojars/v/quamolit/phlox.svg)](https://clojars.org/quamolit/phlox)

```edn
[quamolit/phlox "0.1.3-a1"]
```

`render!` to add canvas to `<body/>`:

```clojure
(ns app.main
  (:require [phlox.core :refer [defcomp hslx render! create-list
                                rect circle text container graphics]]))

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

### Spec

Add a container:

```edn
{
  :position [1 1]
  :on {
    :pointerdown (fn [])
  }
  :alpha 1
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
  :rotation 1
  :pivot [1 2]
  :alpha 1
}
```

Draw text:

```edn
{
  :text "demo"
  :position [1 1]
  :alpha 1
  :style {
    :fill "red"
    :font-size 14
    :font-family "Hind"
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
    (g :end-fill)
    (g :close-path)
    (g :arc-to {:p1 [200 200], :p2 [240 180], :radius 90})
    (g :arc {:center [260 120], :radius 40, :angle [70 60], :anticlockwise? false})
    (g :bezier-to {:p1 [400 500], :p2 [300 200], :to-p [600 300]})
    (g :quadratic-to {:p1 [400 100], :to-p [500 400]})
  ]
  :position [1 1]
  :pivot [1 2]
  :alpha 1
  :on {
    :pointerdown (fn [])
  }
}
```

Draw star:

```edn
; TODO
```

### Workflow

Workflow https://github.com/Quamolit/phlox-workflow

### License

MIT
