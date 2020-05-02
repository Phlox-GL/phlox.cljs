
(ns phlox.comp.messages
  (:require [phlox.core
             :refer
             [defcomp g hslx rect circle text container graphics create-list]]
            [phlox.check :refer [lilac-event-map dev-check lilac-point]]
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
            [phlox.comp.button :refer [comp-button]]))

(def lilac-message-list
  (vector+ (record+ {:id (string+), :text (string+)} {:exact-keys? true}) {:allow-seq? true}))

(def lilac-messages
  (record+
   {:messages lilac-message-list,
    :color (optional+ (number+)),
    :fill (optional+ (number+)),
    :position (optional+ lilac-point),
    :bottom? (optional+ (boolean+)),
    :on-pointertap (fn+)}
   {:check-keys? true}))

(defcomp
 comp-messages
 (options)
 (dev-check options lilac-messages)
 (let [messages (:messages options)
       bottom? (:bottom? options)
       base-position (or (:position options)
                         (if bottom?
                           [(- js/window.innerWidth 16) (- js/window.innerHeight 16)]
                           [(- js/window.innerWidth 16) 16]))
       on-pointertap (or (:on-pointertap options)
                         (fn [x d!] (println "missing message handler:" x)))]
   (create-list
    :container
    {:position base-position}
    (->> messages
         (map-indexed
          (fn [idx message]
            [(:id message)
             (comp-button
              {:text (:text message),
               :position (if bottom?
                 [0 (- 8 (* 40 (- (count messages) idx)))]
                 [0 (* 40 idx)]),
               :color (:color options),
               :fill (:fill options),
               :align-right? true,
               :on-pointertap (fn [e d!] (on-pointertap message d!))})]))))))
