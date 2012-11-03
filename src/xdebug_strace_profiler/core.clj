(ns xdebug-strace-profiler.core
  (:use [clojure.java.io :only (file reader)])
  (:use [clojure.core :only (read-line)])
  (:import [java.io File]))


(defn get-traces [file-path]
  (->> file-path file reader line-seq (drop 1)))


(defn get-trace-position [trace] (nth trace 4))
(defn get-trace-time [trace] (nth trace 0))
(defn get-trace-level [trace]
  (let [part-1 (-> trace (.split "->") first)
        part-1-clean (.trim part-1)]
    (- (count part-1) (count part-1-clean))))
(defn get-trace-call [trace] (nth trace 3))

(defn split-trace [trace]
  (-> trace (.replaceAll " +" " ") .trim (.split " ") seq))

(defn parse-trace [trace]
  (let [trace-split (split-trace trace)]
    [(get-trace-time trace-split)
     (get-trace-call trace-split)
     (get-trace-level trace)
     (get-trace-position trace-split)]))

(defn parse-traces [traces] (map parse-trace traces))

  
(defn build-profile [traces] traces)

(defn xml-profile [profile] profile)

(defn -main [file-path]
  (-> file-path get-traces parse-traces build-profile xml-profile println))



