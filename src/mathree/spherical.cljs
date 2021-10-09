(ns mathree.spherical
  (:require
   [applied-science.js-interop :as j]
   ["three" :as three]
   [mathree.angle :as angle]
   [mathree.arithmetic :as arith]))


(defprotocol ISpherical
  (almost-equals [s1 s2])
  (clone' [s]))


(extend-type three/Spherical
  Object
  (toString [v] (str (vec v)))

  ISeqable
  (-seq [v] (list (.-radius v) (.-phi v) (.-theta v)))

  ISeq
  (-first [v] (.-radius v))
  (-rest [v] (list (.-phi v) (.-theta v)))

  ILookup
  (-lookup
    ([v k] (-lookup v k nil))
    ([v k not-found]
     (case k
       0 (.-radius v)
       1 (.-phi v)
       2 (.-theta v)
       :radius (.-radius v)
       :phi (.-phi v)
       :theta (.-theta v)
       not-found)))

  ISpherical
  (almost-equals [s1 s2]
    (and
     (arith/almost-equals (:radius s1) (:radius s2))
     (arith/almost-equals (angle/standard-angle-in-radians (:phi s1)) (angle/standard-angle-in-radians (:phi s2)))
     (arith/almost-equals (angle/standard-angle-in-radians (:theta s1)) (angle/standard-angle-in-radians (:theta s2)))))

  (clone' [s1]
    (j/call s1 :clone')))


(defn spherical
  ([]
   (spherical 1 0 0))
  ([radius phi theta]
   (new three/Spherical radius phi theta)))

(defn from-seq [s]
  (let [[r p t] s]
    (spherical r p t)))

(defn from-cartesian-coords [x y z]
  (let [sp (spherical)]
    (j/call sp :setFromCartesianCoords x y z)
    sp))


(comment

  (from-cartesian-coords 0 1 0)

  (almost-equals (spherical 1 1 0) (spherical 1 (+ 1 (* Math/PI 2)) 0))
  
  )

