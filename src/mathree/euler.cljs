(ns mathree.euler
  (:require
   [applied-science.js-interop :as j]
   ["three" :as three]))

(defprotocol IEuler
  (clone' [r] "Returns a new Euler with the same parameters as this one.")
  (equals [r1 r2] "Checks for strict equality of this euler and euler.")
  (reorder [r order] "Resets the euler angle with a new order by creating a quaternion from this euler angle and then setting this euler angle with the quaternion and the new order."))


(extend-type three/Euler
  Object
  (toString [v] (str (vec v)))

  ISeqable
  (-seq [v] (list (.-x v) (.-y v) (.-z v) (.-order v)))

  ISeq
  (-first [v] (.-x v))
  (-rest [v] (list (.-y v) (.-z v) (.-order v)))

  ILookup
  (-lookup
    ([v k] (-lookup v k nil))
    ([v k not-found]
     (case k
       0 (.-x v)
       1 (.-y v)
       2 (.-z v)
       3 (.-order v)
       :x (.-x v)
       :y (.-y v)
       :z (.-z v)
       :order (.-order v)
       not-found)))

  IEuler

  (clone' [v1] (j/call v1 :clone))
  (equals [v1 v2]
    (j/call v1 :equals v2))

  (reorder [e order]
    (let [ec (clone' e)]
      (j/call ec :reorder order)))

  ;;    
  )


(defn euler
  "Constructor
Euler( x : Float, y : Float, z : Float, order : String )
x - (optional) the angle of the x axis in radians. Default is 0.
y - (optional) the angle of the y axis in radians. Default is 0.
z - (optional) the angle of the z axis in radians. Default is 0.
order - (optional) a string representing the order that the rotations are applied, defaults to 'XYZ' (must be upper case).
"
  ([] (three/Euler.))
  ([order] (three/Euler. 0 0 0 order))
  ([x y z] (three/Euler. x y z))
  ([x y z order] (three/Euler. x y z order)))


(defn from-seq
  "convert a seq of 3 elements to vector3, default order id XYZ.
   or give order as second argument
   "
  ([sq]
   (from-seq sq "XYZ"))
  ([sq order]
   (let [[x y z] sq]
     (euler x y z order))))

(defn from-quaternion
  "q - a normalized quaternion.
   order - (optional) a string representing the order that the rotations are applied.
   Sets the angles of this euler transform from a normalized quaternion based on the orientation specified by order."
  ([q]
   (from-quaternion q "XYZ"))
  ([q order]
   (let [e1 (euler order)]
     (j/call e1 :setFromQuaternion q order))))


(comment

  (def e1 (euler 1 2 3))
  (vec e1)
  (vec (reorder e1 :XZY))

  (j/call e1 :toVector3)

  )