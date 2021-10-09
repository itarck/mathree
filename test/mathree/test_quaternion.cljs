(ns mathree.test-quaternion
  (:require
   [cljs.test :refer-macros [deftest is testing run-tests]]
   [mathree.quaternion :as q :refer [quaternion quatn]]
   [mathree.vector3 :as v3]
   [mathree.euler :as e]
   [mathree.matrix4 :as m4]))


(def q1 (quaternion 1 2 3 5))
(def q2 (quatn [1 2 3 5]))
(def q3 (q/identity-quaternion))
(def q4 (quaternion 5 4 3 2))

(def nq1 (q/normalize q1))
(def nq4 (q/normalize q4))

(def nv3 (v3/normalize (v3/vector3 1 2 3)))

(def e1 (e/euler 1 2 3))

(def q5 (q/from-euler e1))

(def q6 (q/from-euler (e/euler 0 0 (/ Math/PI -8))))


(deftest test-quatn
  (let [oq1 (q/clone' q1)
        oq2 (q/clone' q2)]

    (is (q/equals q1 q2))
    (is (q/equals (q/conjugate q1) (q/quaternion -1 -2 -3 5)))

    (is (= 1 (q/length (q/normalize q1))))

    (is (q/equals q3 (q/multiply (q/invert nq1) nq1)))
    (is (q/equals (q/multiply q1 (quaternion 1 0 0 0)) (quaternion 5 3 -2 -1)))


    (is (q/equals (q/slerp nq1 nq4 0) nq1))
    (is (q/equals (q/slerp nq1 nq4 1) nq4))

    (is (q/almost-equals
         (q/from-unit-vectors (v3/vector3 1 0 0) (v3/vector3 0 1 0))
         (q/from-axis-angle (v3/vector3 0 0 1) (/ Math/PI 2))))

    (is (q/almost-equals q6
                          (q/from-rotation-matrix
                           (m4/make-rotation-from-quaternion q6))))
    ;; 
    (is (q/equals q1 oq1))
    (is (q/equals q2 oq2))))


(run-tests)


(comment

  (def q1 (q/from-axis-angle (v3/vector3 1 0 0) (/ Math/PI 2)))
  q1

  (v3/apply-quaternion (v3/vector3 0 0 1) q1)

  (def q2 (q/from-axis-angle (v3/vector3 0 0 1) (/ Math/PI 2)))

  (vec q1)
  ;; => [0.7071067811865475 0 0 0.7071067811865476]

  (:x q1)
  ;; => 0.7071067811865475
  (:y q1)
  ;; => 0
  (second q1)
  ;; => 0


  (->
   (v3/vector3 0 0 1)
   (v3/apply-quaternion q1)
   (v3/apply-quaternion q2))
  ;; => #object[Vector3 [1 -2.220446049250313e-16 2.220446049250313e-16]]



  (-> (v3/vector3 0 0 1)
      (v3/apply-quaternion (q/multiply q2 q1)))
  ;; => #object[Vector3 [1 -2.220446049250313e-16 2.220446049250313e-16]]


  (-> (v3/vector3 0 0 1)
      (v3/apply-quaternion (q/multiply q1 q2)))
  ;; => #object[Vector3 [0 -1 2.220446049250313e-16]]


  
  ;; 
  )