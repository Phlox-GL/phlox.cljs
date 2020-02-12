
Phlox
----

> Pixi.js DSL in ClojureScript, inspired by Virtual DOMs. Currently only a small subset of Pixi.js features is supported, good part is how swapping on code changes is available.

Previews http://repo.quamolit.org/phlox/ .

### Usage

[![Clojars Project](https://img.shields.io/clojars/v/quamolit/phlox.svg)](https://clojars.org/quamolit/phlox)

```edn
[quamolit/phlox "0.1.0-a1"]
```

`render!` to add canvas to `<body/>`:

```clojure
(ns app.main
  (:require [phlox.core :refer [defcomp hslx render! create-list
                                rect circle text container graphics]]))

(defcomp comp-demo [data]
  (rect
   {:options {:x 800, :y 40, :width 60, :height 34},
    :fill (hslx 40 80 80),
    :on {:pointerdown (fn [e d!] (d! :demo nil))}}
   (text
    {:text "Demo",
     :position {:x 808, :y 44},
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
  :position {
    :x 1
    :y 1
  }
  :on {
    :pointerdown (fn [])
  }
  :alpha 1
}
```

Draw a circle:

```edn
{
  :options {
    :x 1,
    :y 1,
    :radius 1
  }
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
  :options {
    :x 1,
    :y 1,
    :width 1
    :height 1
  }
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
  :pivot {:x 1, :y 2}
  :alpha 1
}
```

Draw text:

```edn
{
  :text "demo"
  :positiion {:x 1, :y 1}
  :alpha 1
  :style {
    :fill "red"
    :font-size 14
    :font-family "Hind"
  }
}
```

Draw graphics:

```edn
{
  :ops [
    [:move-to {:x 1, :y 1}]
    [:line-to {:x 1, :y 1}]
    [:line-style {}]
    [:begin-fill {:color "red"}]
    [:circle {:x 1, :y 1, :r 2}]
    [:end-fill]
    [:close-path]
    [:arc-to 'TODO]
    [:arc 'TODO]
    [:bezier-curve-to 'TODO]
    [:quadratic-curve-to 'TODO]
  ]
  :position {:x 1, :y 1}
  :pivot {:x 1, :y 2}
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
