
(ns phlox.keyboard )

(defn get-value [*x] @*x)

(defn handle-event [kind tree event dispatch!]
  (when (some? tree)
    (case (:phlox-node tree)
      :component (recur kind (:tree tree) event dispatch!)
      :element
        (do
         (let [listener (get-in tree [:props :on-keyboard kind])]
           (when (fn? listener) (listener event dispatch!)))
         (doall
          (->> (:children tree)
               (map (fn [[k child]] (handle-event kind child event dispatch!))))))
      (do (js/console.log "unknown tree for handling event:" tree)))))

(defn wrap-event [event]
  {:event event,
   :key (.-key event),
   :key-code (.-keyCode event),
   :ctrl? (.-ctrlKey event),
   :meta? (.-metaKey event),
   :shift? (.-shiftKey event)})

(defn handle-keyboard-events [*tree-element dispatch!]
  (.addEventListener
   js/window
   "keydown"
   (fn [event] (handle-event :down (get-value *tree-element) (wrap-event event) dispatch!)))
  (.addEventListener
   js/window
   "keyup"
   (fn [event] (handle-event :up (get-value *tree-element) (wrap-event event) dispatch!)))
  (.addEventListener
   js/window
   "keypress"
   (fn [event] (handle-event :press (get-value *tree-element) (wrap-event event) dispatch!))))
