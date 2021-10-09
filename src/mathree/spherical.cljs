(ns mathree.spherical
  (:require
   [applied-science.js-interop :as j]
   ["three" :as three]
   [mathree.angle :as angle]
   [mathree.arithmetic :as arith]))


(defprotocol ISpherical
  (almost-equals [s1 s2] "Return true if s1 and s2 are almost equal. every element is less than 0.000001.")
  (clone' [s] "Returns a new spherical with the same radius, phi and theta properties as this one."))


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
  "Constructor
Spherical( radius : Float, phi : Float, theta : Float )
radius - the radius, or the Euclidean distance (straight-line distance) from the point to the origin. Default is 1.0.
phi - polar angle in radians from the y (up) axis. Default is 0.
theta - equator angle in radians around the y (up) axis. Default is 0.

The poles (phi) are at the positive and negative y axis. The equator (theta) starts at positive z."
  ([]
   (spherical 1 0 0))
  ([radius phi theta]
   (new three/Spherical radius phi theta)))

(defn from-seq 
  "Convert seq of 3 elements to spherical point"
  [s]
  (let [[r p t] s]
    (spherical r p t)))

(defn from-cartesian-coords 
  "Sets values of this spherical's radius, phi and theta properties from Cartesian coordinates."
  [x y z]
  (let [sp (spherical)]
    (j/call sp :setFromCartesianCoords x y z)
    sp))

(defn from-vector3
  "Sets values of this spherical's radius, phi and theta properties from the Vector3. "
  [vector3]
  (let [sp (spherical)]
    (j/call sp :setFromVector3 vector3)
    sp))



(comment

  (from-cartesian-coords 0 1 0)

  (almost-equals (spherical 1 1 0) (spherical 1 (+ 1 (* Math/PI 2)) 0))
  
  )

