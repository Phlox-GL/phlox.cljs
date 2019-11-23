
Phlox(WIP)
----

> Pixi.js DSL in ClojureScript

### Usage

WIP

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
    :color "red"
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

Workflow https://github.com/mvc-works/calcit-workflow

### License

MIT
