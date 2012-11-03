(ns xdebug-strace-profiler.core
  (:use [clojure.java.io :only (file reader)])
  (:use [clojure.core :only (read-line)])
  (:import [java.io File]))


(defn get-traces [file-path]
  (->> file-path file reader line-seq (drop 1)))




(defn get-trace-time [trace]
  (nth trace 0))

(defn get-trace-level [trace]
  42)

(defn get-trace-call [trace]
  (nth trace 3))

(defn split-trace [trace]
  (-> trace (.replaceAll " +" " ") .trim (.split " ") seq))

(defn parse-trace [trace]
  (let [trace-split (split-trace trace)
        trace-time (get-trace-time trace-split)
        trace-call (get-trace-call trace-split)]
    [trace-time trace-call]))


(defn parse-traces [traces]
  (map parse-trace traces))

  
(defn build-profile [traces]
  traces)

(defn xml-profile [profile]
  profile)

(defn -main [file-path]
  (-> file-path get-traces parse-traces build-profile xml-profile println))



