(ns mathree.arithmetic
  (:require
   [goog.math :as gmath]))


(defn almost-equal?
  " nearlyEquals (a, b, opt_tolerance) → boolean
    opt_tolerance	number=
    Optional tolerance range. Defaults to 0.000001. If specified, should be greater than 0."
  ([a b]
   (gmath/nearlyEquals a b))
  ([a b opt_tolerance]
   (gmath/nearlyEquals a b opt_tolerance)))


(defn almost-all-equal?
  "almost-equal? for every items"
  ([a b]
   (every? (fn [n] (almost-equal? n 0.0)) (map - a b)))
  ([a b opt_tolerance]
   (every? (fn [n] (almost-equal? n 0.0 opt_tolerance)) (map - a b))))


(defn rand-sign []
  (if (< (rand) 0.5) -1 1))

(defn log
  ([n]
   (Math/log n))
  ([base n]
   (/ (Math/log n) (Math/log base))))

(defn pow
  ([x]
   (Math/exp x))
  ([base x]
   (Math/pow base x)))

(defn exp [n]
  (Math/exp n))