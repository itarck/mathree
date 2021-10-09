(ns mathree.matrix3
  (:require
   [applied-science.js-interop :as j]
   ["three" :as three]
   [mathree.arithmetic :as arith]))


(defprotocol IMatrix3
  (almost-equals [m1 m2] "Return true if this matrix and m are almost equal. every element is less than 0.000001.")
  (clone' [m] "Creates a new Matrix3 and with identical elements to this one.")
  (determinant [m] "Computes and returns the determinant of this matrix.")
  (equals [m1 m2] "Return true if this matrix and m are equal.")
  (invert [m1] "Inverts this matrix, using the analytic method. You can not invert with a determinant of zero. If you attempt this, the method produces a zero matrix instead.")
  (multiply [m1 m2] "Sets this matrix to m1 * m2.")
  (transpose [m1] "Transposes this matrix in place."))


(extend-type three/Matrix3
  Object
  (toString [m] (str (vec m)))

  ISeqable
  (-seq [m] (seq (:elements m)))

  ISeq
  (-first [m] (first (seq m)))
  (-rest [m] (rest (seq m)))

  ILookup
  (-lookup
    ([m k] (-lookup m k nil))
    ([m k not-found]
     (cond
       (= k :elements) (j/get m :elements)
       (number? k) (get (vec m) k)
       :else nil)))

  IMatrix3

  (almost-equals [m1 m2]
    (every? (fn [n] (arith/almost-equals n 0.0)) (map - (seq m1) (seq m2))))

  (clone' [m]
    (j/call m :clone))

  (determinant [m]
    (j/call m :determinant))

  (equals [m1 m2]
    (j/call m1 :equals m2))

  (invert [m1]
    (let [mm (clone' m1)]
      (j/call mm :invert)))

  (multiply [m1 m2]
    (let [mm (clone' m1)]
      (j/call mm :multiply m2)))

  (transpose [m1]
    (let [mm (clone' m1)]
      (j/call mm :transpose)))
  ;;    
  )


(defn matrix3
  "Constructor
Matrix3()
Creates and initializes the Matrix3 to the 3x3 identity matrix."
  ([]
   (three/Matrix3.))
  ([sq]
   (let [m (three/Matrix3.)]
     (j/call m :fromArray (clj->js sq))
     m)))


(defn identity-matrix3 
  "Resets this matrix to the 3x3 identity matrix:
1, 0, 0
0, 1, 0
0, 0, 1"
  []
  (let [m (matrix3)]
    (j/call m :identity)))

(defn get-normal-matrix 
  "m - Matrix4
   Sets this matrix as the upper left 3x3 of the normal matrix of the passed matrix4. The normal matrix is the inverse transpose of the matrix m."
  [m4]
  (let [m3 (matrix3)]
    (j/call m3 :getNormalMatrix m4)
    m3))

(defn from-matrix4 
  "Set this matrix to the upper 3x3 matrix of the Matrix4 m."
  [m4]
  (let [m3 (matrix3)]
    (j/call m3 :setFromMatrix4 m4)))


(comment

  
  (js->clj (j/get (matrix3) :elements))

  (seq (identity-matrix3))

  (seq (matrix3 (range 10)))

  (almost-equals (matrix3 (range 10)) (matrix3 (range 10)))

  (matrix3 (reverse (range 10)))

  (matrix3))