(ns mathree.vector3
  (:require
   [applied-science.js-interop :as j]
   [mathree.arithmetic :as arith]
   ["three" :as three]))


(defprotocol IVector3
  (almost-equals [v1 v2] "Return true if v1 and v2 are almost equal. every element is less than 0.000001.")
  (add-scalar [v1 s] "Adds the scalar value s to this vector's x, y and z values.")
  (add [v1 v2] "Adds v to this vector.")
  (apply-quaternion [v q] "Applies a Quaternion transform to this vecto")
  (apply-matrix3 [v m] "Multiplies this vector by m")
  (apply-matrix4 [v m] "Multiplies this vector (with an implicit 1 in the 4th dimension) and m, and divides by perspective.")
  (apply-normal-matrix [v m] "Multiplies this vector by normal matrix m and normalizes the result.")
  (apply-axis-angle [v axis angle] ".applyAxisAngle ( axis : Vector3, angle : Float ) : this. axis - A normalized Vector3. angle - An angle in radians.")
  (apply-euler [v e] "Applies euler transform to this vector by converting the Euler object to a Quaternion and applying.")
  (angle-to [v1 v2] "Returns the angle between this vector and vector v in radians.")
  (clone' [v] "Returns a new vector3 with the same x, y and z values as this one.")
  (cross [v1 v2] "Sets this vector to cross product of a and b.")
  (dot [v1 v2] "Calculate the dot product of this vector and v.")
  (equals [v1 v2] "Returns true if the components of this vector and v are strictly equal; false otherwise.")
  (length [v] "Computes the Euclidean length (straight-line length) from (0, 0, 0) to (x, y, z).")
  (normalize [v] "Convert this vector to a unit vector - that is, sets it equal to a vector with the same direction as this one, but length 1.")
  (multiply-scalar [v s] "Multiplies this vector by scalar s.")
  (project-on-plane [v plane-normal-v] "planeNormal - A vector representing a plane normal. Projects this vector onto a plane by subtracting this vector projected onto the plane's normal from this vector.")
  (sub [v1 v2] "Subtracts v from this vector.")
  (->array [v] "array - (optional) array to store this vector to. If this is not provided a new array will be created. offset - (optional) optional offset into the array."))


(extend-type three/Vector3
  Object
  (toString [v] (str (vec v)))

  ISeqable
  (-seq [v] (list (.-x v) (.-y v) (.-z v)))

  ISeq
  (-first [v] (.-x v))
  (-rest [v] (list (.-y v) (.-z v)))

  ILookup
  (-lookup
    ([v k] (-lookup v k nil))
    ([v k not-found]
     (case k
       0 (.-x v)
       1 (.-y v)
       2 (.-z v)
       :x (.-x v)
       :y (.-y v)
       :z (.-z v)
       not-found)))

  IVector3

  (almost-equals [v1 v2]
    (arith/almost-equals-seq (seq v1) (seq v2)))

  (add [v1 v2]
    (let [vc (clone' v1)]
      (j/call vc :add v2)))

  (add-scalar [v1 s]
    (let [vc (clone' v1)]
      (j/call vc :addScalar s)))

  (apply-quaternion [v q]
    (let [vc (clone' v)]
      (j/call vc :applyQuaternion q)))

  (apply-matrix3 [v q]
    (let [vc (clone' v)]
      (j/call vc :applyMatrix3 q)))

  (apply-matrix4 [v q]
    (let [vc (clone' v)]
      (j/call vc :applyMatrix4 q)))

  (apply-normal-matrix [v m3]
    (let [vc (clone' v)]
      (j/call vc :applyNormalMatrix m3)
      vc))

  (apply-axis-angle [v axis angle]
    (let [vc (clone' v)]
      (j/call vc :applyAxisAngle axis angle)))

  (apply-euler [v e]
    (let [vc (clone' v)]
      (j/call vc :applyEuler e)))

  (angle-to [v1 v2]
    (j/call v1 :angleTo v2))

  (clone' [v1]
    (j/call v1 :clone))

  (cross [v1 v2]
    (let [vc (clone' v1)]
      (j/call vc :cross v2)))

  (dot [v1 v2]
    (let [vc (clone' v1)]
      (j/call vc :dot v2)))

  (equals [v1 v2]
    (j/call v1 :equals v2))

  (length [v]
    (j/call v :length))

  (normalize [v] (let [vc (clone' v)]
                   (j/call vc :normalize)))

  (multiply-scalar [v s]
    (let [vc (clone' v)]
      (j/call vc :multiplyScalar s)))

  (project-on-plane [v plane-normal-v]
    (let [vc (clone' v)]
      (j/call vc :projectOnPlane plane-normal-v)))

  (sub [v1 v2]
    (let [vc (clone' v1)]
      (j/call vc :sub v2)))

  (->array [v]
    (j/call v :toArray))
  ;;    
  )


(defn vector3
  "Constructor function
   vector3( x : Float, y : Float, z : Float )
   x - the x value of this vector. Default is 0.
   y - the y value of this vector. Default is 0.
   z - the z value of this vector. Default is 0.
   "
  ([] (three/Vector3.))
  ([x y z] (three/Vector3. x y z)))

(defn from-seq
  "convert a seq of 3 elements to vector3"
  [v1]
  (let [[x y z] (seq v1)]
    (vector3 x y z)))

(defn from-spherical 
  "convert spherical point to vector3 point"
  [s1]
  (let [v (vector3)]
    (j/call v :setFromSpherical s1)
    v))

(defn from-spherical-coords 
  "convert spherical point, radius phi theta as input,  to vector3 point"
  [radius phi theta]
  (let [v (vector3)]
    (j/call v :setFromSphericalCoords radius phi theta)
    v))

(defn random 
  "Sets each component of this vector to a pseudo-random value between 0 and 1, excluding 1."
  []
  (let [v (vector3)]
    (j/call v :random)))


(comment

  (def v1 (vector3 1 2 2))
  (def v2 (vector3 1 3 6))

  v1

  (length (normalize v1))

  (= (type (vector3)) three/Vector3)

  (from-spherical-coords 1 2 3)

  (let [v (random)
        vc (cross v (vector3 0 1 0))]
    (apply-axis-angle v vc (* Math/PI 0.5)))

  ;; 
  )