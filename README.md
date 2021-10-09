# Mathree

ClojureScript wrapper for Threejs math library. 

In order to keep the illusion of immutable data structures, there is one data copy inside most functions.
If you care about performance, you can still use [js-interop][] to manipulate js mutable data structures.

Most function names are consistent with the threejs Math library, please read the docs.

[js-interop]: https://github.com/applied-science/js-interop

## Installation

Add the following to your project dependencies:

Leiningen/Boot

    [com.github.itarck/mathree "0.1.1-SNAPSHOT"]

Clojure CLI/deps.edn

    com.github.itarck/mathree {:mvn/version "0.1.1-SNAPSHOT"}

## Usage

``` clojure
(ns example.core
  (:require
   [mathree.quaternion :as q :refer [quaternion quatn]]
   [mathree.vector3 :as v3]))


(def q1 (q/from-axis-angle (v3/vector3 1 0 0) (/ Math/PI 2)))
(def q2 (q/from-axis-angle (v3/vector3 0 0 1) (/ Math/PI 2)))

;; method of IVector3 and IQuanternion

(-> (v3/vector3 0 0 1)
    (v3/apply-quaternion q1)
    (v3/apply-quaternion q2))
;; => #object[Vector3 [1 -2.220446049250313e-16 2.220446049250313e-16]]

(-> (v3/vector3 0 0 1)
    (v3/apply-quaternion (q/multiply q2 q1)))
;; => #object[Vector3 [1 -2.220446049250313e-16 2.220446049250313e-16]]

(-> (v3/vector3 0 0 1)
    (v3/apply-quaternion (q/multiply q1 q2)))
;; => #object[Vector3 [0 -1 2.220446049250313e-16]]


;; clojure functions

(vec q1)
;; => [0.7071067811865475 0 0 0.7071067811865476]
(:x q1)
;; => 0.7071067811865475
(second q1)
;; => 0

(v3/normalize (v3/vector3 1 1 0))
;; => #object[Vector3 [0.7071067811865475 0.7071067811865475 0]]

```

## Documentation

* [API Docs](https://cljdoc.org/d/com.github.itarck/mathree/0.1.1-SNAPSHOT/api/mathree)

## Project status

This repo contains only part of the data types of threejs Math library, not all of them, listed below

- [ ] Box2
- [ ] Box3
- [ ] Color
- [ ] Cylindrical
- [x] Euler
- [ ] Frustum
- [ ] Interpolant
- [ ] Line3
- [x] MathUtils
- [x] Matrix3
- [x] Matrix4
- [ ] Plane
- [x] Quaternion
- [ ] Ray
- [ ] Sphere
- [x] Spherical
- [ ] SphericalHarmonics3
- [ ] Triangle
- [ ] Vector2
- [x] Vector3
- [ ] Vector4

## License

Copyright © 2021 itarck, MIT License
