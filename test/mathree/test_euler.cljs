(ns mathree.test-euler
  (:require
   [cljs.test :refer-macros [deftest is testing run-tests]]
   [mathree.euler :as e]
   [mathree.quaternion :as q]
   [mathree.vector3 :as v3]))


(def e1 (e/euler 1 2 3))

e1
;; => #object[Euler [1 2 3 "XYZ"]]

