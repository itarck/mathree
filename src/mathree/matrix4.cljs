(ns mathree.matrix4
  (:require
   [applied-science.js-interop :as j]
   ["three" :as three]
   [mathree.vector3 :as v3]
   [mathree.quaternion :as q]
   [mathree.arithmetic :as arith]))


(defprotocol IMatrix4
  (almost-equals [m1 m2] "Return true if this matrix and m are almost equal. every element is less than 0.000001.")
  (clone' [m] "Creates a new Matrix4 and with identical elements to this one.")
  (determinant [m] "Computes and returns the determinant of this matrix.")
  (decompose [m] "Decomposes this matrix into it's position, quaternion and scale components.")
  (equals [m1 m2] "Return true if this matrix and m are equal.")
  (extract-rotation [m] "Extracts the rotation component of the supplied matrix m into this matrix's rotation component.")
  (invert [m] "Inverts this matrix, using the analytic method. You can not invert with a determinant of zero. If you attempt this, the method produces a zero matrix instead.")
  (multiply [m1 m2] "Sets this matrix to m1 * m2.")
  (scale [m v] "Multiplies the columns of this matrix by vector v.")
  (translate [m v] "Sets the position component for this matrix from vector v,")
  (set-position  [m v] "Sets the position component for this matrix from vector v, without affecting the rest of the matrix - i.e. if the matrix is currently:")
  (transpose [m1] "Transposes this matrix.")

;; 
  )

(defn- switch-row-col [sq]
  (->>
   sq
   (partition 4)
   (apply map list)
   (apply concat)))


(defn matrix4
  "Constructor
   Matrix4()
   Creates and initializes the Matrix4 to the 4x4 identity matrix."
  ([]
   (three/Matrix4.))
  ([sq]
   (let [m (three/Matrix4.)]
     (j/call m :fromArray (clj->js sq))
     m)))

(defn from-col-seq 
  "Sets the elements of this matrix based on an array in column-major format."
  [sq]
  (let [m (three/Matrix4.)]
    (j/call m :fromArray (clj->js sq))
    m))

(defn from-row-seq 
  "Set the elements of this matrix to the supplied row-major values n11, n12, ... n44."
  [sq]
  (let [m (three/Matrix4.)]
    (j/apply m :set (clj->js sq))
    m))

(extend-type three/Matrix4
  Object
  (toString [m]
    (apply str (map (fn [s] (str "\n       " (apply str (interpose ", " s))))
                    (partition 4 (:row-seq m)))))

  ISeqable
  (-seq [m] (:row-seq m))

  ISeq
  (-first [m] (first (seq m)))
  (-rest [m] (rest (seq m)))

  ILookup
  (-lookup
    ([m k] (-lookup m k nil))
    ([m k not-found]
     (cond
       (or (= k :elements) (= k :col-seq)) (seq (j/get m :elements))
       (= k :row-seq) (switch-row-col (seq (j/get m :elements)))
       (number? k) (get (vec m) k)
       :else nil)))

  IMatrix4
  
  (almost-equals [m1 m2]
    (every? (fn [n] (arith/almost-equals n 0.0)) (map - (seq m1) (seq m2))))
  
  (clone' [m]
    (j/call m :clone))

  (determinant [m]
    (j/call m :determinant))

  (equals [m1 m2]
    (j/call m1 :equals m2))

  (decompose [m]
    (let [p1 (v3/vector3)
          q1 (q/quaternion)
          s1 (v3/vector3)]
      (j/call m :decompose p1 q1 s1)
      [p1 q1 s1]))

  (extract-rotation [m]
    (let [m1 (matrix4)]
      (j/call m1 :extractRotation m)
      m1))

  (invert [m]
    (let [m1 (clone' m)]
      (j/call m1 :invert)))

  (multiply [m1 m2]
    (let [mm (clone' m1)]
      (j/call mm :multiply m2)))

  (scale [m v]
    (let [mc (clone' m)]
      (j/call mc :scale v)))

  (translate [m v]
    (let [mc (clone' m)]
      (j/call mc :setPosition v)))

  (set-position [m v]
    (let [mc (clone' m)]
      (j/call mc :setPosition v)))

  (transpose [m1]
    (let [mm (clone' m1)]
      (j/call mm :transpose)))
  ;;    
  )


(defn identity-matrix4 
  "Resets this matrix to the identity matrix."
  []
  (let [m (matrix4)]
    (j/call m :identity)))

(defn compose 
  "Sets this matrix to the transformation composed of position, quaternion and scale."
  [position euler-or-quaternion scale]
  (let [m (matrix4)]
    (cond
      (= (type euler-or-quaternion) three/Euler) (j/call m :compose position (q/from-euler euler-or-quaternion) scale)
      (= (type euler-or-quaternion) three/Quaternion) (j/call m :compose position euler-or-quaternion scale)
      :else  (throw (js/Error. "incorrect rotation type")))))


(defn make-rotation-x 
  "theta — Rotation angle in radians.

Sets this matrix as a rotational transformation around the X axis by theta (θ) radians. The resulting matrix will be:
1 0      0        0
0 cos(θ) -sin(θ)  0
0 sin(θ) cos(θ)   0
0 0      0        1
"
  [theta]
  (let [m (matrix4)]
    (j/call m .makeRotationX theta)))


(defn make-rotation-y 
  "theta — Rotation angle in radians.

Sets this matrix as a rotational transformation around the Y axis by theta (θ) radians. The resulting matrix will be:
cos(θ)  0 sin(θ) 0
0       1 0      0
-sin(θ) 0 cos(θ) 0
0       0 0      1"
  [theta]
  (let [m (matrix4)]
    (j/call m .makeRotationY theta)))


(defn make-rotation-z 
  "theta — Rotation angle in radians.

Sets this matrix as a rotational transformation around the Z axis by theta (θ) radians. The resulting matrix will be:
cos(θ) -sin(θ) 0 0
sin(θ) cos(θ)  0 0
0      0       1 0
0      0       0 1"
  [theta]
  (let [m (matrix4)]
    (j/call m .makeRotationZ theta)))


(defn make-rotation-axis 
  "axis — Rotation axis, should be normalized.
theta — Rotation angle in radians."
  [axis theta]
  (let [m (matrix4)]
    (j/call m .makeRotationAxis axis theta)))

(defn make-basis 
  "Set this to the basis matrix consisting of the three provided basis vectors:
xAxis.x, yAxis.x, zAxis.x, 0,
xAxis.y, yAxis.y, zAxis.y, 0,
xAxis.z, yAxis.z, zAxis.z, 0,
0,       0,       0,       1"
  [x-axis y-axis z-axis]
  (let [m (matrix4)]
    (j/call m .makeBasis x-axis y-axis z-axis)))

(defn make-rotation-from-euler 
  "Sets the rotation component (the upper left 3x3 matrix) of this matrix to the rotation specified by the given Euler Angle. The rest of the matrix is set to the identity. Depending on the order of the euler, there are six possible outcomes. See this page for a complete list."
  [e]
  (let [m (matrix4)]
    (j/call m .makeRotationFromEuler e)))

(defn make-rotation-from-quaternion 
  "Sets the rotation component of this matrix to the rotation specified by q, as outlined here. The rest of the matrix is set to the identity. So, given q = w + xi + yj + zk, the resulting matrix will be:
1-2y²-2z²    2xy-2zw    2xz+2yw    0
2xy+2zw      1-2x²-2z²  2yz-2xw    0
2xz-2yw      2yz+2xw    1-2x²-2y²  0
0            0          0          1"
  [q]
  (let [m (matrix4)]
    (j/call m .makeRotationFromQuaternion q)))

(defn make-scale
  "x - the amount to scale in the X axis.
y - the amount to scale in the Y axis.
z - the amount to scale in the Z axis.

Sets this matrix as scale transform:
x, 0, 0, 0,
0, y, 0, 0,
0, 0, z, 0,
0, 0, 0, 1
"
  [x y z]
  (let [m (matrix4)]
    (j/call m .makeScale x y z)))

(defn make-shear
  "xy - the amount to shear X by Y.
xz - the amount to shear X by Z.
yx - the amount to shear Y by X.
yz - the amount to shear Y by Z.
zx - the amount to shear Z by X.
zy - the amount to shear Z by Y.

Sets this matrix as a shear transform:
1,   yx,  zx,  0,
xy,   1,  zy,  0,
xz,  yz,   1,  0,
0,    0,   0,  1"
  [x y z]
  (let [m (matrix4)]
    (j/call m .makeShear x y z)))

(defn make-translation 
  "x - the amount to translate in the X axis.
y - the amount to translate in the Y axis.
z - the amount to translate in the Z axis.

Sets this matrix as a translation transform:
1, 0, 0, x,
0, 1, 0, y,
0, 0, 1, z,
0, 0, 0, 1
"
  [x y z]
  (let [m (matrix4)]
    (j/call m .makeTranslation x y z)))



(comment

  (def m1 (matrix4))

  (seq (matrix4))


  )