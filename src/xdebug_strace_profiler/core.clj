(ns xdebug-strace-profiler.core
  (:use [clojure.java.io :only (file reader)])
  (:use [clojure.core :only (read-line)])
  (:import [java.io File]))


(defn get-traces [file-path]
  (-> file-path file reader line-seq))

(defn parse-traces [file]  
  file)

(defn build-profile [traces]
  traces)

(defn xml-profile [profile]
  profile)

(defn -main [file-path]
  (-> file-path get-traces parse-traces build-profile xml-profile println))



