(ns mathree.angle
  (:require
   [goog.math :as gmath]))


;; 1. degrees

(defn standard-angle-in-degrees
  "standardAngle (angle) → number
   Normalizes an angle to be in range [0-360). Angles outside this range will be normalized to be the equivalent angle with that range."
  [angle]
  (gmath/standardAngle angle))

(defn to-degrees
  "toDegrees (angleRadians) → number"
  [angle-in-radians]
  (gmath/toDegrees angle-in-radians))

(defn parse-degrees 
  "convert angle in degrees to parsed format of degrees, minutes and seconds"
  [angle-in-degrees]
  (let [degree (int angle-in-degrees)
        rem-degree (rem angle-in-degrees 1)
        minute (int (* 60 rem-degree))
        rem-minute (rem (* 60 rem-degree) 1)
        second (* rem-minute 60)]
    {:degrees degree
     :minutes minute
     :seconds second}))

(defn calculate-degrees 
  "convert parsed format to angle in degrees"
  [parsed-degrees]
  (let [{:keys [degrees minutes seconds]} parsed-degrees]
    (+ degrees
       (/ minutes 60)
       (/ seconds 3600))))


;; 2. radians

(defn standard-angle-in-radians
  "standardAngleInRadians (angle) → number
   Normalizes an angle to be in range [0-2*PI). Angles outside this range will be normalized to be the equivalent angle with that range."
  [angle-in-radians]
  (gmath/standardAngleInRadians angle-in-radians))

(defn to-radians
  "toRadians (angleDegrees) → number"
  [angle-in-degree]
  (gmath/toRadians angle-in-degree))

